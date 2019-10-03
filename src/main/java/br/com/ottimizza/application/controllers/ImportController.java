package br.com.ottimizza.application.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.application.domain.dtos.ImportDataModel;
import br.com.ottimizza.application.services.UserService;

@RestController
@RequestMapping("/api/v1/import")
public class ImportController {

    private Map<String, DeferredResult<String>> suspendedTradeRequests = new ConcurrentHashMap<String, DeferredResult<String>>();

    @Inject
    UserService userService;

    // ...

    @ResponseBody
    @PostMapping("/csv")
    public HttpEntity<?> upload(@RequestParam("file") MultipartFile file, Principal principal) throws Exception {
        return ResponseEntity.ok("");
    }

    @ResponseBody
    @PostMapping("/json")
    public HttpEntity<?> upload(@RequestBody List<ImportDataModel> data, Principal principal) throws Exception {
        return ResponseEntity.ok(userService.importUsersFromJSON(data, principal));
    }

    @PostMapping("/json3")
    public @ResponseBody DeferredResult<String> upload2(@RequestBody List<ImportDataModel> data, Principal principal)
            throws Exception {
        DeferredResult<String> deferredResult = new DeferredResult<String>();

        CompletableFuture.runAsync(() -> {
            deferredResult.setResult("Task Finished 1");
            try {
                // Long pooling task;If task is not completed within 100 sec timeout response
                // retrun for this request
                userService.importUsersFromJSON(data, principal);
                // set result after completing task to return response to client
                // deferredResult.setResult("Task Finished 1");
            } catch (Exception ex) {
            }
        });
        return deferredResult;
    }

    @GetMapping("/test")
    DeferredResult<String> test() {
        final String uuid = UUID.randomUUID().toString();

        Long timeOutInMilliSec = TimeUnit.MINUTES.toMillis(1L);
        String timeOutResp = "Timeout";
        DeferredResult<String> deferredResult = new DeferredResult<>(timeOutInMilliSec, timeOutResp);

        //
        suspendedTradeRequests.put(uuid, deferredResult);

        CompletableFuture.runAsync(() -> {
            try {
                // Long pooling task;If task is not completed within 100 sec timeout response
                // retrun for this request
                TimeUnit.SECONDS.sleep(10);
                // set result after completing task to return response to client
                suspendedTradeRequests.get(uuid).setResult("Task Finished 1");
            } catch (Exception ex) {
            }
        });

        deferredResult.onCompletion(() -> {
            System.out.println("Finished");
        });

        return deferredResult;
    }

    static class DeferredQuote extends DeferredResult<String> {
        private final String symbol;

        public DeferredQuote(String symbol) {
            this.symbol = symbol;
        }
    }

    private final Queue<DeferredQuote> responseQueue = new ConcurrentLinkedQueue<>();

    @RequestMapping("/{uid}")
    public @ResponseBody DeferredQuote deferredResult(@PathVariable("uid") String uid) {
        DeferredQuote result = new DeferredQuote(uid);
        responseQueue.add(result);
        return result;
    }

    @Scheduled(fixedRate = 2000)
    public void processQueues() {
        for (DeferredQuote result : responseQueue) {
            System.out.println("it");
            result.setResult(result.symbol);

            responseQueue.remove(result);
        }
    }

}
//
let organizationCnpjField = document.getElementById('organization-cnpj-input');
let passwordField = document.getElementById('password-input');
let passwordCheckField = document.getElementById('password-check-input');

// password strength meter 
let passwordStrengthMeter = document.getElementById('password-strength-meter');
let passwordStrengthText = document.getElementById('password-strength-text');
const passwordStrengths = {
  1: "Pior", 2: "Ruim", 3: "Fraca", 4: "Boa", 5: "Forte"
};
let passwordStrengthChecker = () => {
  let password = passwordField.value || '';
  if (password == '') return;

  let result = zxcvbn(val);
  let score = result.score + 1; // 

  passwordStrengthMeter.value = score;
  passwordStrengthMeter.style.display = 'block';
  passwordStrengthText.innerHTML = `Força: ${passwordStrengths[score]}`;
}

// on password input, checks the password strength.
passwordField.addEventListener('input', passwordStrengthChecker);

(function (window, document, $) {
  'use strict';
  // Input, Select, Textarea validations except submit button
  $("input,select,textarea").not("[type=submit]").jqBootstrapValidation();
})(window, document, jQuery);

// var strength = {
//   1: "Pior",
//   2: "Ruim",
//   3: "Fraca",
//   4: "Boa",
//   5: "Forte"
// }

// var meter = document.getElementById('password-strength-meter');
// var text = document.getElementById('password-strength-text');

// passwordField.addEventListener('input', function () {
//   let val = passwordField.value;
//   let result = zxcvbn(val);

//   // Update the password strength meter
//   meter.value = result.score + 1;
//   meter.style.display = 'block';

//   // Update the text indicator
//   if (val !== "") {
//     text.innerHTML = "Força: " + strength[result.score + 1];
//   } else {
//     text.innerHTML = "";
//   }
// });

let checkPasswordMatch = function () {
  if (passwordField.value == passwordCheckField.value) {
    return true;
  } else {
    return false;
  }
}

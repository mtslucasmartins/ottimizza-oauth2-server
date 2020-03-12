const passwordField = document.getElementById('password');
function passwordFieldTypeToggle(e) {
  const passwordFieldToggle = document.querySelector('#passwordFieldToggle');
  const passwordFieldToggleIcon = document.querySelector('#passwordFieldToggle i');

  // For IE9
  let classes = passwordFieldToggleIcon.className.split(" ");
  let iEyeOff = classes.indexOf("ft-eye-off");

  console.log(classes);

  toggleInputType(passwordField, 'password', 'text');
  if (iEyeOff >= 0) {
    classes.splice(iEyeOff, 1);
    classes.push("ft-eye");
  } else {
    classes.push("ft-eye-off");
    classes.splice(classes.indexOf("ft-eye"), 1);
  }
  passwordFieldToggleIcon.className = classes.join(" ");
}

function toggleInputType(el, t1, t2) {
  el.type = (el.type === t2) ? t1 : t2;
}

function toggleCssClasses(el, c1, c2) {
  if (containsCssClass(el, c1)) {
    appendCssClass(el, c2);
    removeCssClass(el, c1);
  } else {
    appendCssClass(el, c1);
    removeCssClass(el, c2);
  }
}
function containsCssClass(el, c) {
  return el.className.split(" ").indexOf(c) >= 0;
}
function removeCssClass(el, c) {
  let classes = el.className.split(" ");
  el.className = classes.splice(classes.indexOf(c), 1).join(" ");
}
function appendCssClass(el, c) {
  el.className = el.className.split(" ").append(c).join(" ");
}

//
//
//
let form = document.getElementById('form-login')
let submitButton = document.getElementById('btn-login');

form.addEventListener('submit', function() {
  submitButton.setAttribute('disabled', 'disabled');
}, false);
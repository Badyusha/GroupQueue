//  IF YOU GOOD WITH JS => YOU BETTER DO NOT WATCH CODE BELLOW

// FUNCS
async function sleep(ms) {
    return new Promise(resolve=>setTimeout(resolve, ms));
}

async function sendMessageWithDelay(invalidMessage, firstMessage, secondMessage, delay, color='red') {
    invalidMessage.style.color = 'var(--'+color+')';
    invalidMessage.innerHTML = firstMessage;
    await sleep(delay);
    invalidMessage.innerHTML = secondMessage;
}

function numberIsInRange(num, min, max) {
    return num >= min && num <= max;
}

async function fetchData(requestText) {
    let response = await fetch(requestText);
    return await response.text();
}



// GLOBAL VARS
let firstNameIsOk = true;
let lastNameIsOk = true;
let groupNumberIsOk = true;
let usernameIsOk = true;
let passwordIsOk = true;
let repeatedPasswordIsOk = true;

const signUpButton = document.getElementById('sign-up-form-button');
const invalidMessage = document.getElementById('invalid-sign-up-request');

const USERNAME_MIN_LEN = 7;
const USERNAME_MAX_LEN = 15;
const NAME_MIN_LEN = 3;
const NAME_MAX_LEN = 20;

const PSSWD_MIN_LEN = 4;

const invalidBorder = '0.15em solid var(--red)';
const firstNameInput = document.getElementById('first-name-input');
const lastNameInput = document.getElementById('last-name-input');
const groupNumberInput = document.getElementById('group-number-input');
const usernameInput = document.getElementById('sign-up-username-input');
const passwordInput = document.getElementById('sign-up-password-input');
const repeatedPasswordInput = document.getElementById('repeated-password-input');

// EVENT LISTENERS
groupNumberInput.addEventListener('blur', async function () {
    if(groupNumberInput.value.length === 0) {
        groupNumberInput.style.borderBottom = '';
        return;
    }

    let groupExists = (await fetchData('/group/number/'+groupNumberInput.value+'/exists') === 'true');
    if(groupExists) {
        groupNumberIsOk = true;
        groupNumberInput.style.borderBottom = '';
        await sendMessageWithDelay(invalidMessage, '', '', 4000);
        return;
    }

    groupNumberIsOk = false;
    groupNumberInput.style.borderBottom = invalidBorder;
    await sendMessageWithDelay(invalidMessage, 'Invalid group number', '', 4000);
});

usernameInput.addEventListener('blur', async function() {
    if(usernameInput.value.length === 0) {
        usernameInput.style.borderBottom = '';
        return;
    }

    let usernameExists = (await fetchData('/user/username/'+usernameInput.value+'/exists') === 'true');

    if(!numberIsInRange(usernameInput.value.length, USERNAME_MIN_LEN, USERNAME_MAX_LEN)) {
        usernameIsOk = false;
        usernameInput.style.borderBottom = invalidBorder;
        await sendMessageWithDelay(invalidMessage, 'username.length is NOT in range['+USERNAME_MIN_LEN+
            ','+USERNAME_MAX_LEN+']', '', 4000);
        return;
    }

    if(usernameExists) {
        usernameInput.style.borderBottom = invalidBorder;
        usernameIsOk = false;
        await sendMessageWithDelay(invalidMessage, 'Username is already in use', '', 4000);
        return;
    }

    usernameInput.style.borderBottom = '';
    usernameIsOk = true;
    await sendMessageWithDelay(invalidMessage, '', '', 4000);
});

firstNameInput.addEventListener('blur', async function() {
    if(firstNameInput.value.length === 0) {
        firstNameInput.style.borderBottom = '';
        return;
    }

    if(!numberIsInRange(firstNameInput.value.length, NAME_MIN_LEN, NAME_MAX_LEN)) {
        firstNameIsOk = false;
        firstNameInput.style.borderBottom = invalidBorder;
        await sendMessageWithDelay(invalidMessage, 'firstName.length is NOT in range['+NAME_MIN_LEN+
            ','+NAME_MAX_LEN+']', '', 4000);
        return;
    }

    firstNameIsOk = true;
    firstNameInput.style.borderBottom = '';
    await sendMessageWithDelay(invalidMessage, '', '', 4000);
});

lastNameInput.addEventListener('blur', async function() {
    if(lastNameInput.value.length === 0) {
        lastNameInput.style.borderBottom = '';
        return;
    }

    if(!numberIsInRange(lastNameInput.value.length, NAME_MIN_LEN, NAME_MAX_LEN)) {
        lastNameIsOk = false;
        lastNameInput.style.borderBottom = invalidBorder;
        await sendMessageWithDelay(invalidMessage, 'firstName.length is NOT in range['+NAME_MIN_LEN+
            ','+NAME_MAX_LEN+']', '', 4000);
        return;
    }

    lastNameIsOk = true;
    lastNameInput.style.borderBottom = '';
    await sendMessageWithDelay(invalidMessage, '', '', 4000);
});

repeatedPasswordInput.addEventListener('blur', async function() {
    if(repeatedPasswordInput.value.length === 0) {
        repeatedPasswordInput.style.borderBottom = '';
        return;
    }

    if(passwordInput.value !== repeatedPasswordInput.value) {
        repeatedPasswordIsOk = false;
        passwordInput.style.borderBottom = invalidBorder;
        repeatedPasswordInput.style.borderBottom = invalidBorder;
        await sendMessageWithDelay(invalidMessage, 'Passwords does NOT match', '', 4000);
        return;
    }

    repeatedPasswordIsOk = true;
    passwordInput.style.borderBottom = '';
    repeatedPasswordInput.style.borderBottom = '';
    await sendMessageWithDelay(invalidMessage, '', '', 4000);
});

passwordInput.addEventListener('blur', async function() {
    if(passwordInput.value.length === 0) {
        passwordInput.style.borderBottom = '';
        return;
    }

    if(passwordInput.value.length < PSSWD_MIN_LEN) {
        passwordIsOk = false;
        passwordInput.style.borderBottom = invalidBorder;
        await sendMessageWithDelay(invalidMessage, 'password.length is NOT in range[4,...)', '', 4000);
        return;
    }

    if(passwordInput.value === repeatedPasswordInput.value) {
        repeatedPasswordIsOk = true;
        passwordInput.style.borderBottom = '';
        repeatedPasswordInput.style.borderBottom = '';
        await sendMessageWithDelay(invalidMessage, '', '', 4000);
    }

    passwordIsOk = true;
    passwordInput.style.borderBottom = '';
    await sendMessageWithDelay(invalidMessage, '', '', 4000);
});



signUpButton.addEventListener('click', async function () {
    event.preventDefault();

    if (firstNameInput.value.length === 0 || lastNameInput.value.length === 0 || groupNumberInput.value.length === 0 ||
        usernameInput.value.length === 0 || passwordInput.value.length === 0 || repeatedPasswordInput.value.length === 0) {
        await sendMessageWithDelay(invalidMessage, 'NOT all fields are filled', '', 4000);
        return;
    }

    if (!firstNameIsOk || !lastNameIsOk || !usernameIsOk
        || !groupNumberIsOk || !passwordIsOk || !repeatedPasswordIsOk) {
        await sendMessageWithDelay(invalidMessage, 'Some fields are invalid', '', 4000);
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/user/registration',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            firstName: firstNameInput.value,
            lastName: lastNameInput.value,
            groupNumber: groupNumberInput.value,
            username: usernameInput.value,
            password: passwordInput.value
        }),
        success: function (response) {
            window.location.replace('/user/main_page');
        },
        error: async function (response) {
            await sendMessageWithDelay(invalidMessage, 'System error', '', 4000);
            console.error("Error while registration: " + response.data)
        }
    });
});

// FUNCTIONS
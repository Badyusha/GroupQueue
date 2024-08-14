const darkOverlay = document.getElementById('dark-overlay');
const editProfileForm = document.getElementById('edit-profile-form');

const firstNameInput = document.getElementById('first-name-input');
const lastNameInput = document.getElementById('last-name-input');
const usernameInput = document.getElementById('sign-up-username-input');
const passwordInput = document.getElementById('sign-up-password-input');
const groupNumberInput = document.getElementById('group-number-input');
const repeatedPasswordInput = document.getElementById('repeated-password-input');

const cancelButton = document.getElementById('cancel-change-profile-button');
const saveButton = document.getElementById('save-profile-button');

const invalidMessage = document.getElementById('error-label');
const invalidBorder = '0.15em solid var(--red)';

const USERNAME_MIN_LEN = 7;
const USERNAME_MAX_LEN = 15;
const NAME_MIN_LEN = 3;
const NAME_MAX_LEN = 20;

const PSSWD_MIN_LEN = 4;

let firstNameIsOk = true;
let lastNameIsOk = true;
let groupNumberIsOk = true;
let usernameIsOk = true;
let passwordIsOk = true;
let repeatedPasswordIsOk = true;

document.getElementById('edit-profile').addEventListener('click',
                                                            async function() {

    fillProfileInputs();

    darkOverlay.classList.toggle('active');
    sideMenu.classList.remove('active');
    menuOverlay.classList.remove('active');
    editProfileForm.style.display = 'block';
});

darkOverlay.addEventListener('click', function() {
   darkOverlay.classList.remove('active');
   editProfileForm.style.display = 'none';
});

cancelButton.addEventListener('click', function() {
    darkOverlay.classList.remove('active');
    editProfileForm.style.display = 'none';
});



groupNumberInput.addEventListener('blur', async function () {
    if(groupNumberInput.value.length === 0) {
        groupNumberInput.style.borderBottom = '';
        return;
    }

    if(groupNumberInput.value == groupNumber) {
        groupNumberIsOk = true;
        groupNumberInput.style.borderBottom = '';
        await sendMessageWithDelay(invalidMessage, '', '', 4000);
        return;
    }

    let groupExists = (await fetchData('/group/number/'+groupNumberInput.value+'/exists') === true);
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

    if(usernameInput.value == username) {
        usernameIsOk = true;
        usernameInput.style.borderBottom = '';
        await sendMessageWithDelay(invalidMessage, '', '', 4000);
        return;
    }

    let usernameExists = (await fetchData('/user/username/'+usernameInput.value+'/exists') === true);
    if(!numberIsInRange(usernameInput.value.length, USERNAME_MIN_LEN, USERNAME_MAX_LEN)) {
        usernameIsOk = false;
        usernameInput.style.borderBottom = invalidBorder;
        sendMessageWithDelay(invalidMessage, 'username.length is NOT in range['+USERNAME_MIN_LEN+
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
        sendMessageWithDelay(invalidMessage, 'firstName.length is NOT in range['+NAME_MIN_LEN+
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
        sendMessageWithDelay(invalidMessage, 'firstName.length is NOT in range['+NAME_MIN_LEN+
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
        sendMessageWithDelay(invalidMessage, 'Passwords does NOT match', '', 4000);
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
        sendMessageWithDelay(invalidMessage, 'password.length is NOT in range[4,...]', '', 4000);
        return;
    }

    if(passwordInput.value === repeatedPasswordInput.value) {
        repeatedPasswordIsOk = true;
        passwordInput.style.borderBottom = '';
        repeatedPasswordInput.style.borderBottom = '';
        sendMessageWithDelay(invalidMessage, '', '', 4000);
    }

    passwordIsOk = true;
    passwordInput.style.borderBottom = '';
    await sendMessageWithDelay(invalidMessage, '', '', 4000);
});

saveButton.addEventListener('click', function() {
    event.preventDefault();

    if(firstNameInput.value.length === 0 || lastNameInput.value.length === 0 || groupNumberInput.value.length === 0 ||
        usernameInput.value.length === 0 || (passwordInput.value.length !== 0 && repeatedPasswordInput.value.length === 0))
    {
        sendMessageWithDelay(invalidMessage, 'NOT all fields are filled', '', 4000);
        return;
    }

    if(!firstNameIsOk || !lastNameIsOk || !usernameIsOk
        || !groupNumberIsOk || !passwordIsOk || !repeatedPasswordIsOk)
    {
        sendMessageWithDelay(invalidMessage, 'Some fields are invalid', '', 4000);
        return;
    }

    if(isAllFieldsNotChanged()) {
        sendMessageWithDelay(invalidMessage, 'No changes made', '', 4000);
    }

    $.ajax({
        type: 'POST',
        url: '/user/edit_profile',
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
        success: async function(response) {
            await sendMessageWithDelay(invalidMessage, 'Profile edited', '', 1000, 'green');
            darkOverlay.classList.remove('active');
            editProfileForm.style.display = 'none';
            window.location.reload();
        },
        error: function(response) {
            sendMessageWithDelay(invalidMessage, 'System error', '', 4000);
            console.error("Error while registration: " + response.data)
        }
    });

});

function isAllFieldsNotChanged() {
    return passwordInput.value.length === 0 && firstNameInput.value === firstName &&
        lastNameInput.value === lastName && Number.parseInt(groupNumberInput.value) === groupNumber &&
        usernameInput.value === username;
}


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
    return await response.json();
}




function fillProfileInputs() {
    firstNameInput.value = firstName;
    lastNameInput.value = lastName;
    usernameInput.value = username;
    groupNumberInput.value = groupNumber;
    passwordInput.value = '';
    repeatedPasswordInput.value = '';
}
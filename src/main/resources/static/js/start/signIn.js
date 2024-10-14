const username = document.getElementById('sign-in-username-input');
const password = document.getElementById('sign-in-password-input');

const signInButton = document.getElementById('sign-in-form-button');
const errorMessage = document.getElementById('invalid-sign-in-request');

signInButton.addEventListener('click', async function () {
    event.preventDefault();


    if (username.value.length === 0 || password.value.length === 0) {
        await sendMessageWithDelay(errorMessage, 'Some fields are empty', '', 4000);
        return;
    }

    $.ajax({
        type: 'POST',
        url: '/student/authorization',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            username: username.value,
            password: password.value
        }),
        success: function (response) {
            window.location.replace('/student/main_page');
        },
        error: async function (response) {
            await sendMessageWithDelay(errorMessage, 'Incorrect username or password', '', 4000);
            console.error("Error while registration: " + response.data)
        }
    });
});
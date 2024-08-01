document.addEventListener('DOMContentLoaded', function() {
    // Elements
    const signInButton = document.getElementById('sign-in-button');
    const signInButtonHref = document.getElementById("sign-in-button-ref");
    const signInPopupForm = document.getElementById('sign-in-popup-form');
    const signUpButton = document.getElementById('sign-up-button');
    const signUpButtonHref = document.getElementById("sign-up-button-ref");
    const signUpPopupForm = document.getElementById('sign-up-popup-form');
    const overlay = document.getElementById('overlay');

    // Show the popup form and overlay
    signInButton.addEventListener('click', function() {
        overlay.style.display = 'block';
        signInPopupForm.style.display = 'block';
    });

    signInButtonHref.addEventListener('click', function() {
        overlay.style.display = 'block';
        signInPopupForm.style.display = 'block';
        signUpPopupForm.style.display = 'none';
    });

    signUpButton.addEventListener('click', function() {
        overlay.style.display = 'block';
        signUpPopupForm.style.display = 'block';
    });

    signUpButtonHref.addEventListener('click', function() {
        overlay.style.display = 'block';
        signInPopupForm.style.display = 'none';
        signUpPopupForm.style.display = 'block';
    });

    // Hide the popup form and overlay when clicking outside the form
    overlay.addEventListener('click', function() {
        overlay.style.display = 'none';
        signInPopupForm.style.display = 'none';
        signUpPopupForm.style.display = 'none';
    });
});

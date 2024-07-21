document.addEventListener('DOMContentLoaded', function() {
    // Elements
    const signInButton = document.getElementById('sign-in-button');
    const overlay = document.getElementById('overlay');
    const popupForm = document.getElementById('popup-form');

    // Show the popup form and overlay
    signInButton.addEventListener('click', function() {
        overlay.style.display = 'block';
        popupForm.style.display = 'block';
    });

    // Hide the popup form and overlay when clicking outside of the form
    overlay.addEventListener('click', function() {
        overlay.style.display = 'none';
        popupForm.style.display = 'none';
    });
});

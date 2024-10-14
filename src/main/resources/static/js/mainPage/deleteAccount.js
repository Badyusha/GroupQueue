const deleteAccountForm = document.getElementById('delete-account-container');
const becomeGroupAdminForm = document.getElementById('become-group-admin-container');
const cancelDeleteAccountButton = document.getElementById('cancel-delete-account-button');
const deleteAccountButton = document.getElementById('delete-account-button');
const deleteAccountError = document.getElementById('delete-account-error');

cancelDeleteAccountButton.addEventListener('click', function() {
   deleteAccountForm.style.display = 'none';
   becomeGroupAdminForm.style.display = 'none';
   overlay.classList.remove('active');
});

function showDeleteAccountForm() {
    sideMenu.classList.remove('active');
    menuOverlay.classList.remove('active');
    deleteAccountForm.style.display = 'block';
    overlay.classList.toggle('active');
}

deleteAccountButton.addEventListener('click', function() {
    $.ajax({
        type: 'DELETE',
        url: '/student/delete',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: async function (response) {
            await sendMessageWithDelay(deleteAccountError, 'Successfully deleted', '', 1500, 'green');
            deleteAccountForm.style.display = 'none';
            becomeGroupAdminForm.style.display = 'none';
            overlay.classList.remove('active');
            window.location.replace('/');
        },
        error: async function (response) {
            await sendMessageWithDelay(deleteAccountError, 'System error', '', 4000);
            console.error("Error while registration: " + response.data)
        }
    });
});
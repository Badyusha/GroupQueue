const cancelBecomeGroupAdminButton = document.getElementById('cancel-become-group-admin-button');
const becomeGroupAdminButton = document.getElementById('become-group-admin-button');

cancelBecomeGroupAdminButton.addEventListener('click', function() {
   overlay.classList.remove('active');
   becomeGroupAdminForm.style.display = 'none';
});

becomeGroupAdminButton.addEventListener('click', function() {
    const becomeGroupAdminError = document.getElementById('become-group-admin-error');
    $.ajax({
        type: 'POST',
        url: '/request/send/become_group_admin',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: async function () {
            await sendMessageWithDelay(becomeGroupAdminError, 'Request sent', '', 1500, 'green');
            overlay.classList.remove('active');
            becomeGroupAdminForm.style.display = 'none';
        },
        error: async function (response) {
            await sendMessageWithDelay(becomeGroupAdminError, 'Cannot send request', '', 2000);
            console.error("Error while registration: " + response.data)
        }
    });
});
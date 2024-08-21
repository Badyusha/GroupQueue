const leaveLabRegistrationContainer = document.getElementById('leave-lab-registration');
const cancelLeavingRegistrationButton = document.getElementById('cancel-lab-registration-button');
const leaveRegistrationButton = document.getElementById('leave-lab-registration-button');
const errorLeaveLabel = document.getElementById('leave-queue-error');

let leaveLessonId = 0;

cancelLeavingRegistrationButton.addEventListener('click', function() {
    overlay.classList.remove('active');
    leaveLabRegistrationContainer.style.display = 'none';
});

leaveRegistrationButton.addEventListener('click', function() {
   event.preventDefault();
    $.ajax({
        type: 'DELETE',
        url: '/pre_queue/remove/user',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(leaveLessonId),
        success: async function (response) {
            await sendMessageWithDelay(errorLeaveLabel, 'Successfully leaved', '', 1500, 'green');
            overlay.classList.remove('active');
            leaveLabRegistrationContainer.style.display = 'none';
            window.location.reload();
        },
        error: async function (response) {
            await sendMessageWithDelay(errorLeaveLabel, 'System error', '', 4000);
            console.error("Error while registration: " + response.data)
        }
    });
});

function showLeaveLabRegistrationForm(lessonId) {
    leaveLessonId = lessonId;
    leaveLabRegistrationContainer.style.display = 'block';
    overlay.classList.toggle('active');
}
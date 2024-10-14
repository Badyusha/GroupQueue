const registerToQueueContainer =  document.getElementById('register-to-queue-container');
const cancelRegisterButton = document.getElementById('cancel-register-to-queue-button');
const registerToQueueButton = document.getElementById('register-to-queue-button');

const subjectName = document.getElementById('subject-name');
const subgroupType = document.getElementById('subgroup');
const startTimeElem = document.getElementById('start-time');
const passingLabsList = document.getElementById('passing-labs');
const invalidLabsListLabel = document.getElementById('invalid-labs-list-label');

cancelRegisterButton.addEventListener('click', function() {
    overlay.classList.remove('active');
    registerToQueueContainer.style.display = 'none';
    passingLabsList.value = '';
});

let registrationLessonId = 0;
let dayOfWeek = '';
let startTime = '';
registerToQueueButton.addEventListener('click', async function () {
    event.preventDefault();

    let passingLabs = passingLabsList.value;
    let passingLastListError = isPassingLabsListOk(passingLabs);
    if (passingLastListError.length !== 0) {
        await sendMessageWithDelay(invalidLabsListLabel, passingLastListError, '', 4000);
        return;
    }

    let passingLabsNumbersList = getPassingLabsList(passingLabs);
    $.ajax({
        type: 'POST',
        url: '/pre_queue/register/student',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            lessonId: registrationLessonId,
            passingLabs: passingLabsNumbersList,
            dayOfWeek: dayOfWeek,
            startTime: startTime
        }),
        success: async function (response) {
            await sendMessageWithDelay(invalidLabsListLabel, 'Successfully registered', '', 1500, 'green');
            overlay.classList.remove('active');
            registerToQueueContainer.style.display = 'none';
            window.location.reload();
        },
        error: async function (response) {
            await sendMessageWithDelay(invalidLabsListLabel, 'System error', '', 3000);
            console.error("Error while registration: " + response.data)
        }
    });
});

function showLabRegistrationForm(lessonId, _startTime, _subjectName, subgroup, _dayOfWeek) {
    registrationLessonId = lessonId;
    startTime = _startTime;
    dayOfWeek = _dayOfWeek;
    registerToQueueContainer.style.display = 'block';
    overlay.classList.toggle('active');

    subjectName.value = _subjectName;
    subgroupType.value = (subgroup === 'all') ? 'all' : subgroup.at(0);
    startTimeElem.value = _startTime;
}

function isPassingLabsListOk(text) {
    if(text.length === 0) {
        return 'Labs list is empty'
    }
    const regex = /^\d{1,2}(\s*,\s*\d{1,2}|\s+\d{1,2})*$/;

    if (!regex.test(text)) {
        return 'Labs list does not match the template';
    }

    const numbers = getPassingLabsList(text);
    const uniqueNumbers = new Set();

    for (let number of numbers) {
        if (number < 1 || number > 20) {
            return 'labNumber is NOT in range [1,20]';
        }

        if (uniqueNumbers.has(number)) {
            return 'Labs list should contain unique numbers';
        }

        uniqueNumbers.add(number);
    }
    return '';
}

function getPassingLabsList(text) {
    return text.split(/[,\s]+/).map(Number);
}
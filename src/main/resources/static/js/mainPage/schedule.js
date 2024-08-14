async function fetchData(requestText) {
    let response = await fetch(requestText);
    let schedule = await response.json();
    return schedule;
}

function formatDate(dateString) {
    const [year, month, day] = dateString.split('-');

    return `${day}.${month}`;
}

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function removeTextAfterParenthesis(input) {
    // Find the index of '('
    const index = input.indexOf('(');

    // If '(' is found, slice the string up to the index of '('
    // Otherwise, return the original string
    return index !== -1 ? input.slice(0, index) : input;
}


function removeSecondsFromTime(timeString) {
    // Check if the timeString is in the format "HH:MM:SS"
    if (timeString.length === 8 && timeString[2] === ':' && timeString[5] === ':') {
        // Slice the string to remove the last 3 characters
        return timeString.slice(0, -3);
    }
    // Return the original string if it's not in the expected format
    return timeString;
}

function signToQueue(lessonId) {
    console.log(lessonId);
}

function cancelRegister(lessonId) {
    console.log(lessonId);
}

function showQueue(queueId) {
    console.log(queueId);
}

function drawSchedule(schedule) {
    for (const dayOfWeek in schedule) {
        let dayCell = document.getElementById(`${dayOfWeek}-schedule`);
        if (!dayCell) continue;

        let dayOfWeekDate = document.getElementById(`${dayOfWeek}`);
        dayOfWeekDate.innerHTML = `${capitalizeFirstLetter(dayOfWeek)} (${formatDate(schedule[dayOfWeek].date)})`;

        dayCell.innerHTML = '';
        let dayContent = '';

        schedule[dayOfWeek].lessons.forEach(lesson => {
            let lessonId = lesson.lessonId;
            let subjectName = removeTextAfterParenthesis(lesson.subjectName);
            let subjectFullName = lesson.subjectFullName;
            let startTime = removeSecondsFromTime(lesson.startTime);
            let subgroupType = (lesson.subgroupType == 'ALL') ? 'общ.' :
                                                (lesson.subgroupType == 'FIRST') ? '1 под.' : '2 под.';
            let isRegisteredInQueue = lesson.registeredInQueue;
            let numberInQueue = lesson.numberInQueue;
            let queueId = lesson.queueId;
            let status = `
                            <div class="status">
                                <a onclick="signToQueue(${lessonId})" class="sign-up-to-queue">
                                    Записаться
                                </a>
                            </div>
            `;

            if(isRegisteredInQueue) {
                status = `
                    <div style="display: grid; margin-top: 0.4em; margin-right: 1em">
                        <label class="signed-up-status">
                            Записан
                        </label>
                        <a onclick="cancelRegister(${lessonId})" class="cancel-register-status">
                            Отменить запись?
                        </a>
                    </div>
                `;
            }

            if(numberInQueue !== null) {
                status = `
                    <div class="status">
                        <a onclick="showQueue(${queueId})" class="number-in-queue-text-status">
                            Ты <label style="color: white">${numberInQueue}</label> в очереди
                        </a>
                    </div>
                `;
            }

            dayContent += `
                <div class="lesson">
                    <div class="lesson-container">
                        <div class="subject-date-cabinet-subgroup">
                            <div class="subject-date">
                                <label class="subject" title="${subjectFullName}">${subjectName}</label>
                                <label class="date">${startTime}</label>
                            </div>
                            <div class="subgroup">
                                <label class="subgroup">${subgroupType}</label>
                            </div>
                            ${status}
                        </div>
                    </div>
                </div>
            `;
        });

        dayCell.innerHTML = dayContent;
    }
}

document.addEventListener('DOMContentLoaded', async function() {
    let schedule = await fetchData('/schedule/get');
    await fillSideMenu();
    drawSchedule(schedule);
});


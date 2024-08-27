const overlay = document.getElementById('dark-overlay');


document.addEventListener('DOMContentLoaded', async function() {
    await drawSchedule();
    await fillSideMenu();
    await fillCurrentWeek();
});

async function fetchData(requestText) {
    let response = await fetch(requestText);
    return await response.json();
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

function showQueue(queueId) {
    console.log(queueId);
}

async function fillCurrentWeek() {
    try {
        let currentWeek = await fetchData('/week/get/current');
        document.getElementById('current-week').innerHTML = `Current week: ${currentWeek}`;
    } catch(e) {
        console.error('cannot get response from /week/get/current');
    }
}

async function drawSchedule() {
    let schedule;
    try {
        schedule = await fetchData('/schedule/get');
    } catch(e) {
        let scheduleTable = document.getElementById('week-schedule-table');
        scheduleTable.deleteRow(0);
        scheduleTable.deleteRow(0);

        let tableRowHeader = document.createElement('tr');
        let noDataHeader = document.createElement('th');
        noDataHeader.innerText = "No data found";
        scheduleTable.appendChild(noDataHeader);

        noDataHeader.style.textAlign = 'center';
        noDataHeader.style.color = 'var(--inputText)';
        noDataHeader.style.paddingBottom = '1em';

        scheduleTable.append(tableRowHeader);
        scheduleTable.deleteRow(0);

        console.error('cannot get response from /schedule/get');
        return;
    }
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
            let subgroupType = (lesson.subgroupType === 'ALL') ? 'all' :
                                                (lesson.subgroupType === 'FIRST') ? '1 sub.' : '2 sub.';
            let isRegisteredInQueue = lesson.registeredInQueue;
            let numberInQueue = lesson.numberInQueue;
            let queueId = lesson.queueId;
            let isRegistrationOpen = lesson.registrationOpen;

            let status = `
                            <div class="status">
                                <a onclick="showLabRegistrationForm(${lessonId}, '${startTime}',
                                                            '${subjectName}', '${subgroupType}', '${dayOfWeek}')" 
                                                            class="register-to-queue">
                                    Register
                                </a>
                            </div>
            `;
            if(!isRegistrationOpen) {
                status = `
                    <div class="status">

                    </div>
                `
            }

            if (isRegisteredInQueue) {
                status = `
                    <div class="leave-status">
                        <label class="registered-up-status">
                            Registered
                        </label>
                        <a onclick="showLeaveLabRegistrationForm(${lessonId})" class="leave-register-status">
                            Leave?
                        </a>
                    </div>
                `;
            }

            if(numberInQueue !== null) {
                status = `
                    <div class="queue-position-status">
                        <a href="/user/queues" class="number-in-queue-text-status">
                            You are <span style="color: white">${numberInQueue}</span> in Q
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


overlay.addEventListener('click', function() {
    overlay.classList.remove('active');
    registerToQueueContainer.style.display = 'none';
    leaveLabRegistrationContainer.style.display = 'none';
    deleteAccountForm.style.display = 'none';
    becomeGroupAdminForm.style.display = 'none';
    passingLabsList.value = '';
});
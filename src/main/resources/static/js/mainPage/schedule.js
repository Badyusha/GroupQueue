async function fetchData(requestText) {
    let response = await fetch(requestText);
    let lessonsSchedule = await response.text();
    return JSON.parse(lessonsSchedule);
}

function getWeekStartDate() {
    let today = new Date();
    return new Date(today.setDate(today.getDate() - today.getDay() + 1)); // Monday
}

function formatDate(date) {
    return date.toLocaleDateString('en-GB', { day: '2-digit', month: '2-digit' }).replace(/\//g, '.');
}

let daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

function updateTableHeaders() {
    let startDate = getWeekStartDate();

    daysOfWeek.forEach((day, index) => {
        let currentDay = new Date(startDate);
        currentDay.setDate(startDate.getDate() + index);
        let formattedDate = formatDate(currentDay);
        document.getElementById(day).innerText = `${day} (${formattedDate})`;
    });
}

function findFirstLessonForDayOfWeek(data, dayOfWeek) {
    for(let i = 0; i < data.length; ++i) {
        if(data.at(i).dayOfWeek.toUpperCase() === dayOfWeek.toUpperCase()) {
            let lessonCopy = data.at(i);
            data.splice(i, 1);
            return lessonCopy;
        }
    }
    return null;
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

function addLessonIntoRow(oneTableRowLessons, table) {
    let row = table.insertRow();

    for(let i = 0; i < oneTableRowLessons.length; ++i) {
        let cell = row.insertCell(i);

        if(oneTableRowLessons.at(i) == null) {
            cell.innerHTML = '';
            continue;
        }
        
        let scheduleId = oneTableRowLessons.at(i).id;
        let subject = removeTextAfterParenthesis(oneTableRowLessons.at(i).subjectName);
        let subjectFullName = oneTableRowLessons.at(i).subjectFullName
        let startTime = removeSecondsFromTime(oneTableRowLessons.at(i).startTime);
        let subgroup = (oneTableRowLessons.at(i).subgroupType === 'ALL') ? 'общ.' :
                                ((oneTableRowLessons.at(i).subgroupType === 'FIRST') ? '1 под.' : '2 под.');

        cell.innerHTML = `
                     <div class="lesson">
                         <div id="${scheduleId}" class="lesson-container">
                             <div class="subject-date-cabinet-subgroup">
                                 <div class="subject-date">
                                     <label class="subject" title="${subjectFullName}">${subject}</label>
                                     <label class="date">${startTime}</label>
                                 </div>
                                 <div class="subgroup">
                                     <label class="subgroup">${subgroup}</label>
                                 </div>
                                 <div class="status">
                                     <a onclick="signToQueue(${scheduleId})" class="sign-up-to-queue">Записаться</a>
                                 </div>
                             </div>
                         </div>
                     </div>
        `;
    }

}

function signToQueue(scheduleId) {

}

function insertRowForFewLabs(schedule, table) {
    let tableRowLessons = [];
    for(let i = 0; i < daysOfWeek.length; ++i) {
        let lessonOfDay = findFirstLessonForDayOfWeek(schedule, daysOfWeek.at(i));
        tableRowLessons.push(lessonOfDay);
    }
    addLessonIntoRow(tableRowLessons, table);
}

function populateSchedule(schedule) {
    let scheduleCopy = schedule.map((x) => x);
    let table = document.getElementById('week-schedule-table');
    let oneTableRowLessons = [];
    let isRowAdded = false;

    for(let daysOfWeekIndex = 0; schedule.length !== 0; ++daysOfWeekIndex) {
        if(daysOfWeekIndex > daysOfWeek.length - 1) {
            addLessonIntoRow(oneTableRowLessons, table);
            oneTableRowLessons = [];
            daysOfWeekIndex = 0;
            isRowAdded = true;
        }
        let lessonOfDay = findFirstLessonForDayOfWeek(schedule, daysOfWeek.at(daysOfWeekIndex));
        oneTableRowLessons.push(lessonOfDay);
    }

    if(oneTableRowLessons.length != 0) {
        insertRowForFewLabs(schedule, table);
        return;
    }

    if(!isRowAdded) {
        insertRowForFewLabs(scheduleCopy, table);
    }
}

document.addEventListener('DOMContentLoaded', async function() {
    let schedule = await fetchData('/schedule/get');
    updateTableHeaders();
    populateSchedule(schedule);
});

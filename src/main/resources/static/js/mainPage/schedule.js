async function fetchData(requestText) {
    let response = await fetch(requestText);
    return await response.json();
}

function getWeekStartDate() {
    let today = new Date();
    return new Date(today.setDate(today.getDate() - today.getDay() + 1)); // Monday
}

function formatDate(date) {
    return date.toLocaleDateString('en-GB', { day: '2-digit', month: '2-digit' });
}

function updateTableHeaders() {
    let startDate = getWeekStartDate();
    let daysOfWeek = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];

    daysOfWeek.forEach((day, index) => {
        let currentDay = new Date(startDate);
        currentDay.setDate(startDate.getDate() + index);
        let formattedDate = formatDate(currentDay);
        document.getElementById(day).innerText = `${day} (${formattedDate})`;
    });
}

function populateSchedule(schedule) {
    console.log(schedule);
    schedule.forEach(item => {
        let day = new Date(item.date).toLocaleDateString('en-GB', { weekday: 'long' });
        let dayCell = document.getElementById(day);

        let lessonHTML = `
            <td>
                <div class="lesson">
                    <div id="${item.id}" class="lesson-container">
                        <div class="subject-date-cabinet-subgroup">
                            <div class="subject-date">
                                <label id="subject">${item.subject}</label>
                                <label id="date">${item.time}</label>
                            </div>
                            <div class="cabinet-subgroup">
                                <label id="cabinet">${item.cabinet}</label>
                                <label id="subgroup">${item.subgroup}</label>
                            </div>
                            <div class="href">
                                <a href="${item.link}" class="sign-up-to-queue">Записаться</a>
                            </div>
                        </div>
                    </div>
                </div>
            </td>
        `;

        dayCell.innerHTML += lessonHTML;
    });
}

document.addEventListener('DOMContentLoaded', async function() {
    let schedule = await fetchData('/get_schedule');
    updateTableHeaders();
    populateSchedule(schedule);
});

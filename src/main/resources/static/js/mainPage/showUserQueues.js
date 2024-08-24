const overlay = document.getElementById('dark-overlay');
const finalQueue = document.getElementById('final-queue');
const finalTable = document.getElementById('final-queue-table');
const finalTableTbody = document.getElementById('final-table-tbody');
const subjectInfo = document.getElementById('subject-info');

overlay.addEventListener('click', function() {
    overlay.classList.remove('active');
    finalQueue.style.display = 'none';

    for (let i = 0; i < finalTable.rows.length; ++i) {
        finalTable.deleteRow(1);
    }
})



async function fetchData(requestText) {
    try {
        let response = await fetch(requestText);
        return await response.json();
    } catch (e) {
        console.log(e);
    }
}

function formatDate(dateString) {
    const [year, month, day] = dateString.split('-');

    return `${day}.${month}.${year}`;
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

function decodeBase64(base64String) {
    try {
        // Decode base64 to binary string
        const binaryString = atob(base64String);
        // Convert binary string to array of numbers
        return Array.from(binaryString).map(char => char.charCodeAt(0)).join(', ');
    } catch (error) {
        console.error('N/A', error);
        return '';
    }
}



document.addEventListener('DOMContentLoaded', async function() {
    await fillUserQueues();
});

async function fillUserQueues() {
    let queues = await fetchData('/queue/get');
    if(queues.length === 0) {
        document.getElementById('queues-table').remove();
        let queuesContainer = document.getElementById('queues-container');
        queuesContainer.innerText = 'You have no active registrations :(';
        queuesContainer.style.padding = '1em';
        queuesContainer.style.color = 'var(--gray)';
        queuesContainer.style.fontWeight = '500';
        return;
    }
    insertDataIntoTable(queues);
}

async function showFinalQueue(lessonId, subjectName, date, startTime) {

    overlay.classList.add('active');
    finalQueue.style.display = 'block';

    subjectInfo.textContent = subjectName + ' — ' + formatDate(date) + ' — ' + removeSecondsFromTime(startTime);

    let usersQueue = await fetchData(`/queue/lesson/${lessonId}/get`);

    usersQueue.forEach((data, index) => {
        const row = document.createElement('tr');

        row.innerHTML = `
        <td>${index + 1}</td>
        <td>${data.firstName}</td>
        <td>${data.lastName}</td>
        <td>${data.username}</td>
        <td>${decodeBase64(data.passingLabs)}</td>
    `;

        finalTableTbody.appendChild(row);
    });

}

async function insertDataIntoTable(data) {
    const table = document.getElementById('queues-table');

    // Iterate over each item in the data array
    data.forEach(item => {
        const row = document.createElement('tr');

        const subjectNameCell = document.createElement('td');
        subjectNameCell.textContent = item.subjectName;
        row.appendChild(subjectNameCell);

        const dateCell = document.createElement('td');
        dateCell.textContent = formatDate(item.date);
        row.appendChild(dateCell);

        const startTimeCell = document.createElement('td');
        startTimeCell.textContent = removeSecondsFromTime(item.startTime);
        row.appendChild(startTimeCell);

        const subgroupTypeCell = document.createElement('td');
        subgroupTypeCell.textContent = (item.subgroupType === 'ALL') ? 'All' :
            (item.subgroupType === 'FIRST' ? 'First' : 'Second');
        row.appendChild(subgroupTypeCell);

        const passingLabsCell = document.createElement('td');

        passingLabsCell.textContent = item.passingLabs ? decodeBase64(item.passingLabs) : 'N/A';
        row.appendChild(passingLabsCell);

        const registrationStatusCell = document.createElement('td');
        let isNumberInQueueNull = item.numberInQueue === -1;

        const numberInQueueCell = document.createElement('td');
        const sortTypeCell = document.createElement('td');
        sortTypeCell.textContent = item.sortType.toLowerCase();

        if (!isNumberInQueueNull) {
            subjectNameCell.setAttribute('onclick', `showFinalQueue(${item.lessonId},
                                                                        '${item.subjectName}', 
                                                                        '${item.date}',
                                                                        '${item.startTime}')`);
            subjectNameCell.style.cursor = 'pointer';

            registrationStatusCell.className = 'status-closed';
            registrationStatusCell.textContent = 'Closed';

            numberInQueueCell.className = 'number-in-queue';
            numberInQueueCell.textContent = item.numberInQueue;

        } else {
            subjectNameCell.title = 'Registration is not finished yet';

            registrationStatusCell.className = 'status-open';
            registrationStatusCell.textContent = 'Open';

            numberInQueueCell.className = 'queue-not-ready';
            numberInQueueCell.title = 'Registration is not finished yet';
            numberInQueueCell.textContent = '—';
        }

        row.appendChild(registrationStatusCell);
        row.appendChild(numberInQueueCell);
        row.appendChild(sortTypeCell);

        table.appendChild(row);
    });
}
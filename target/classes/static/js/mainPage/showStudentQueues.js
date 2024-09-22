const overlay = document.getElementById('dark-overlay');
const finalQueue = document.getElementById('final-queue');
const finalTable = document.getElementById('final-queue-table');
const finalQueueTable = document.getElementById('final-queue-table');
const subjectInfo = document.getElementById('subject-info');

overlay.addEventListener('click', function() {
    overlay.classList.remove('active');
    finalQueue.style.display = 'none';

    for (let i = 0; i < finalTable.rows.length + 1; ++i) {
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
        queuesContainer.innerText = 'You have no active queues';
        queuesContainer.style.padding = '1em';
        queuesContainer.style.color = 'var(--inputText)';
        queuesContainer.style.fontWeight = '600';
        return;
    }
    fillTableHeader();
    await insertDataIntoTable(queues);
}

function fillTableHeader() {
    // Create and append header cells with arrows
    const headers = [
        { text: 'Subject name', index: 0 },
        { text: 'Date', index: 1 },
        { text: 'Start time', index: 2 },
        { text: 'Subgroup', index: 3 },
        { text: 'Passing labs', index: 4 },
        { text: 'Registration status', index: 5 },
        { text: 'Number in Q', index: 6 },
        { text: 'Sort type', index: 7 }
    ];

    const table = document.getElementById('queues-table');
    const tableRowHeader = document.createElement('tr');

    headers.forEach(header => {
        let th = document.createElement('th');
        th.innerHTML = `${header.text}`;
        if (header.index !== 4 && header.index !== 6 && header.index !== 7) {
            th.innerHTML = `${header.text} <span class="arrow">&#9650;</span><span class="arrow">&#9660;</span>`;
        }
        th.style.cursor = 'pointer';

        if (header.index !== 4 && header.index !== 6 && header.index !== 7) {
            th.addEventListener('click', () => sortTable(header.index, th));
        }
        tableRowHeader.appendChild(th);
    });
    table.append(tableRowHeader);

    function sortTable(columnIndex, headerElement) {
        let rows = Array.from(table.querySelectorAll('tr')).slice(1); // exclude header row
        let isAscending = headerElement.getAttribute('data-sort-direction') === 'asc';

        rows.sort((rowA, rowB) => {
            let cellA = rowA.cells[columnIndex].innerText.trim();
            let cellB = rowB.cells[columnIndex].innerText.trim();

            let comparison = 0;

            if (columnIndex === 1) { // Date column
                const parseDate = (dateStr) => {
                    let [day, month, year] = dateStr.split('.');
                    return new Date(`${year}-${month}-${day}`);
                };
                comparison = parseDate(cellA) - parseDate(cellB);
            } else if (columnIndex === 2) { // Start time column
                comparison = cellA.localeCompare(cellB);
            } else if(columnIndex === 0 || columnIndex === 3|| columnIndex === 5){ // Subject name or Sort type column
                comparison = cellA.localeCompare(cellB);
            }

            return isAscending ? comparison : -comparison;
        });

        // Toggle sort direction
        isAscending = !isAscending;
        headerElement.setAttribute('data-sort-direction', isAscending ? 'asc' : 'desc');

        // Update arrow visibility
        headerElement.querySelectorAll('.arrow').forEach(arrow => arrow.style.color = 'var(--gray)');
        if (isAscending) {
            headerElement.querySelector('.arrow:first-child').style.color = 'var(--headerBorder)';
        } else {
            headerElement.querySelector('.arrow:last-child').style.color = 'var(--headerBorder)';
        }

        rows.forEach(row => table.appendChild(row));
    }
}

async function showFinalQueue(lessonId, subjectName, subgroup, date, startTime) {
    overlay.classList.add('active');
    finalQueue.style.display = 'block';

    subjectInfo.textContent = subjectName + '(' + subgroup + ')' + ' — ' + formatDate(date) + ' — ' + removeSecondsFromTime(startTime);

    let usersQueue = await fetchData(`/queue/lesson/${lessonId}/get`);

    usersQueue.forEach((data, index) => {
        const row = document.createElement('tr');

        row.innerHTML = `
        <td>${index + 1}</td>
        <td>${data.firstName}</td>
        <td>${data.username}</td>
        <td>${data.lastName}</td>
        <td>${decodeBase64(data.passingLabs)}</td>
    `;

        finalQueueTable.appendChild(row);
    });

}

function transformText(input) {
    // Convert the input to lowercase
    let lowerCaseText = input.toLowerCase();

    // Replace underscores with spaces
    let spacedText = lowerCaseText.replace(/_/g, ' ');

    // Capitalize the first letter
    return spacedText.charAt(0).toUpperCase() + spacedText.slice(1);
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
        let subgroup = (item.subgroupType === 'ALL') ? 'All' :
            (item.subgroupType === 'FIRST' ? '1' : '2');
        subgroupTypeCell.textContent = subgroup;
        row.appendChild(subgroupTypeCell);

        const passingLabsCell = document.createElement('td');
        let passingLabs = item.passingLabs ? decodeBase64(item.passingLabs) : 'N/A';

        const registrationStatusCell = document.createElement('td');
        let isNumberInQueueNull = item.numberInQueue === -1;

        if(isNumberInQueueNull) {
            passingLabsCell.innerHTML = `
                <input id="${item.lessonId}" class="passing-labs-input"
                value="${passingLabs}" onblur="changePassingLabs(${item.lessonId})"
                placeholder="1, 2, 3 or 1,2,3 or 1 2 3">
            `;
        } else {
            passingLabsCell.innerHTML = `
                <input id="${item.lessonId}" class="passing-labs-input"
                value="${passingLabs}" onblur="changePassingLabs(${item.lessonId})"
                placeholder="1, 2, 3 or 1,2,3 or 1 2 3" disabled>
            `;
        }
        row.appendChild(passingLabsCell);

        const numberInQueueCell = document.createElement('td');
        const sortTypeCell = document.createElement('td');
        sortTypeCell.textContent = transformText(item.sortType);

        if (!isNumberInQueueNull) {
            subjectNameCell.setAttribute('onclick', `showFinalQueue(${item.lessonId},
                                                                        '${item.subjectName}', 
                                                                        '${subgroup}',
                                                                        '${item.date}',
                                                                        '${item.startTime}')`);
            subjectNameCell.style.cursor = 'pointer';

            registrationStatusCell.className = 'status-finished';
            registrationStatusCell.textContent = 'Finished';

            numberInQueueCell.className = 'number-in-queue';
            numberInQueueCell.textContent = item.numberInQueue;

        } else {
            subjectNameCell.title = 'Registration is not finished yet';

            registrationStatusCell.className = 'status-not-finished';
            registrationStatusCell.textContent = 'Not finished';

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

async function changePassingLabs(lessonId) {
    let passingLabs = document.getElementById(lessonId).value;
    if (!isPassingLabsListOk(passingLabs)) {
        alert('Lab number is not in range(1, 20), numbers are not unique or incorrect template');
        document.getElementById(lessonId).style.borderBottomColor = 'var(--red)';
        await sleep(2000);
        document.getElementById(lessonId).style.borderBottomColor = '';
        return false;
    }

    $.ajax({
        type: 'POST',
        url: '/pre_queue/change_passing_labs',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            lessonId: lessonId,
            passingLabs: passingLabs.split(/[,\s]+/).map(Number)
        }),
        success: async function (response) {
            document.getElementById(lessonId).style.borderBottomColor = 'var(--green)';
            await sleep(2000);
            document.getElementById(lessonId).style.borderBottomColor = '';
            window.location.reload();
        },
        error: async function (response) {
            document.getElementById(lessonId).style.borderBottomColor = 'var(--red)';
            await sleep(2000);
            document.getElementById(lessonId).style.borderBottomColor = '';
            alert("System error");
        }
    });
}

async function sleep(ms) {
    return new Promise(resolve=>setTimeout(resolve, ms));
}

function isPassingLabsListOk(text) {
    if(text.length === 0) {
        return false;
    }
    const regex = /^\d{1,2}(\s*,\s*\d{1,2}|\s+\d{1,2})*$/;

    if (!regex.test(text)) {
        return false;
    }

    const numbers = text.split(/[,\s]+/).map(Number);
    const uniqueNumbers = new Set();

    for (let number of numbers) {
        if (number < 1 || number > 20 || uniqueNumbers.has(number)) {
            return false;
        }
        uniqueNumbers.add(number);
    }
    return true;
}
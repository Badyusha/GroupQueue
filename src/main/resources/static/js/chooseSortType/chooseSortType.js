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



document.addEventListener('DOMContentLoaded', async function() {
    let sortTypeOptions = await fetchData('/lesson/sort_type/get');
    let lessons = await fetchData('/schedule/group/get');

    // fill in table header
    let lessonsTable = document.getElementById('lessons-table');
    let tableRowHeader = document.createElement('tr');

    if (lessons.length === 0) {
        let noDataHeader = document.createElement('th');
        noDataHeader.innerText = "No data found";
        tableRowHeader.appendChild(noDataHeader);

        noDataHeader.style.textAlign = 'center';
        noDataHeader.style.color = 'var(--gray)';

        lessonsTable.append(tableRowHeader);
        return;
    }

    // Create and append header cells with arrows
    const headers = [
        { text: 'Subject name', index: 0 },
        { text: 'Subgroup', index: 1 },
        { text: 'Date', index: 2 },
        { text: 'Start time', index: 3 },
        { text: 'Sort type', index: 4 }
    ];

    headers.forEach(header => {
        let th = document.createElement('th');
        th.innerHTML = `${header.text}`;
        if(header.index !== 4) {
            th.innerHTML = `${header.text} <span class="arrow">&#9650;</span><span class="arrow">&#9660;</span>`
        }
        th.style.cursor = 'pointer';

        if(header.index !== 4) {
            th.addEventListener('click', () => sortTable(header.index, th));
        }
        tableRowHeader.appendChild(th);
    });

    lessonsTable.append(tableRowHeader);

    // fill in table body
    lessons.forEach(lesson => {
        let tableRow = document.createElement('tr');

        let subjectName = tableRow.insertCell(0);
        subjectName.innerText = lesson.subjectName;
        subjectName.title = `${lesson.subjectFullName}`;

        let subgroup = tableRow.insertCell(1);
        let subgroupType = lesson.subgroupType;
        subgroup.innerText = (subgroupType === 'ALL') ? 'All' :
            (subgroupType === 'FIRST' ? '1' : '2');

        let date = tableRow.insertCell(2);
        date.innerText = formatDate(lesson.date);

        let startTime = tableRow.insertCell(3);
        startTime.innerText = removeSecondsFromTime(lesson.startTime);

        let sortTypeSelection = tableRow.insertCell(4);
        sortTypeSelection.innerHTML = `<select id="${lesson.lessonId}"></select>`;

        lessonsTable.append(tableRow);

        // fill in selection options
        let selection = document.getElementById(`${lesson.lessonId}`);
        sortTypeOptions.forEach(sortType => {
            let newOption = document.createElement("option");
            newOption.value = sortType;
            newOption.text = sortType;
            selection.add(newOption);
        });

        selection.value = lesson.sortType;

        selection.addEventListener('change', async function () {
            await changeSortType(lesson.lessonId);
        });
    });

    function sortTable(columnIndex, headerElement) {
        let rows = Array.from(lessonsTable.querySelectorAll('tr')).slice(1); // exclude header row
        let isAscending = headerElement.getAttribute('data-sort-direction') === 'asc';

        rows.sort((rowA, rowB) => {
            let cellA = rowA.cells[columnIndex].innerText.trim();
            let cellB = rowB.cells[columnIndex].innerText.trim();

            let comparison = 0;

            if (columnIndex === 2) { // Date column
                const parseDate = (dateStr) => {
                    let [day, month, year] = dateStr.split('.');
                    return new Date(`${year}-${month}-${day}`);
                };
                comparison = parseDate(cellA) - parseDate(cellB);
            } else if (columnIndex === 3) { // Start time column
                comparison = cellA.localeCompare(cellB);
            } else { // Subject name or Sort type column
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

        rows.forEach(row => lessonsTable.appendChild(row));
    }
});


async function sleep(ms) {
    return new Promise(resolve=>setTimeout(resolve, ms));
}

function changeSortType(lessonId) {
    $.ajax({
        type: 'POST',
        url: '/lesson/change/sort_type',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            lessonId: lessonId,
            sortType: document.getElementById(lessonId).value
        }),
        success: async function (response) {
            document.getElementById(lessonId).style.borderBottomColor = 'var(--green)';
            await sleep(2000);
            document.getElementById(lessonId).style.borderBottomColor = '';
        },
        error: async function (response) {
            document.getElementById(lessonId).style.borderBottomColor = 'var(--red)';
            await sleep(2000);
            document.getElementById(lessonId).style.borderBottomColor = '';
            console.error("Error while registration: " + response.data)
        }
    });
}
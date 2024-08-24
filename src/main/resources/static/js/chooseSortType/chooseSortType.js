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

    if(lessons.length === 0) {
        let noDataHeader = document.createElement('th');
        noDataHeader.innerText = "No data found";
        tableRowHeader.appendChild(noDataHeader);

        noDataHeader.style.textAlign = 'center';
        noDataHeader.style.color = 'var(--inputText)';

        lessonsTable.append(tableRowHeader);
        return;
    }

    let subjectNameHeader = document.createElement('th');
    subjectNameHeader.innerText = "Subject name";

    let dateHeader = document.createElement('th');
    dateHeader.innerText = "Date";

    let startTimeHeader = document.createElement('th');
    startTimeHeader.innerText = "Start time";

    let sortTypeSelectionHeader = document.createElement('th');
    sortTypeSelectionHeader.innerHTML = "Sort type";

    tableRowHeader.appendChild(subjectNameHeader);
    tableRowHeader.appendChild(dateHeader);
    tableRowHeader.appendChild(startTimeHeader);
    tableRowHeader.appendChild(sortTypeSelectionHeader);

    lessonsTable.append(tableRowHeader);

    // fill in table body
    lessons.forEach(lesson => {
        let tableRow = document.createElement('tr');

        let subjectName = tableRow.insertCell(0);
        subjectName.innerText = lesson.subjectName;
        subjectName.title = `${lesson.subjectFullName}`;

        let date = tableRow.insertCell(1);
        date.innerText = formatDate(lesson.date);

        let startTime = tableRow.insertCell(2);
        startTime.innerText = removeSecondsFromTime(lesson.startTime);

        let sortTypeSelection = tableRow.insertCell(3);
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
// async function fetchData(requestText) {
//     try {
//         let response = await fetch(requestText);
//         return await response.text();
//     } catch (e) {
//         console.log(e);
//     }
// }
//
// document.addEventListener('DOMContentLoaded', async function() {
//     await fillUserQueues();
// });
//
// async function fillUserQueues() {
//     console.log("queues");
//     let queues = await fetchData('/queue/get');
//     console.log(queues);
// }
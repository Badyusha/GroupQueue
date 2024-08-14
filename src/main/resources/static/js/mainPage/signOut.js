const signOut = document.getElementById('sign-out');

signOut.addEventListener('click', function() {
   console.log("cookie: "+document.cookie);
   window.location.replace('/');
});
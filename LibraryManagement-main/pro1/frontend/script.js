const base = "http://localhost:8000";

let currentUser = null;
let currentRole = null;

function showDashboard() {
    document.getElementById("authSection").classList.add("hidden");
    document.getElementById("dashboard").classList.remove("hidden");
    document.getElementById("usernameLabel").innerText = currentUser;
    document.getElementById("roleLabel").innerText = currentRole;
    if (currentRole === "ADMIN") document.getElementById("adminPanel").classList.remove("hidden");
    loadBooks();
}

function register() {
    const u = document.getElementById("regUser").value;
    const p = document.getElementById("regPass").value;
    fetch(base + "/register", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({ username: u, password: p })
    }).then(r => r.json()).then(d => alert(d.message));
}

function login() {
    const u = document.getElementById("logUser").value;
    const p = document.getElementById("logPass").value;
    fetch(base + "/login", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({ username: u, password: p })
    }).then(r => r.json())
        .then(d => {
            if (d.status === "OK") {
                currentUser = d.username;
                currentRole = d.role;
                showDashboard();
            } else {
                alert(d.message || "Login failed");
            }
        });
}

function logout() {
    currentUser = null;
    currentRole = null;
    document.getElementById("dashboard").classList.add("hidden");
    document.getElementById("authSection").classList.remove("hidden");
}

function loadBooks() {
    fetch(base + "/books")
        .then(r => r.json())
        .then(books => {
            let html = "";
            books.forEach(b => {
                html += `<p><b>ID:</b> ${b.id} — <b>${b.title}</b> by ${b.author} (Qty: ${b.quantity})</p>`;
            });
            document.getElementById("bookList").innerHTML = html;
        }).catch(e => {
        document.getElementById("bookList").innerText = "Failed to load books.";
    });
}

function borrow() {
    if (!currentUser) { alert("Please login."); return; }
    const id = Number(document.getElementById("borrowBookId").value);
    fetch(base + "/borrow", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({ username: currentUser, bookId: id })
    }).then(r => r.json()).then(d => { alert(d.message); loadBooks(); });
}

function returnBook() {
    if (!currentUser) { alert("Please login."); return; }
    const id = Number(document.getElementById("returnBookId").value);
    fetch(base + "/return", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({ username: currentUser, bookId: id })
    }).then(r => r.json()).then(d => { alert(d.message); loadBooks(); });
}

function addBook() {
    if (!currentUser || currentRole !== "ADMIN") { alert("Admin only"); return; }
    const id = Number(document.getElementById("addId").value);
    const title = document.getElementById("addTitle").value;
    const author = document.getElementById("addAuthor").value;
    const qty = Number(document.getElementById("addQty").value);
    fetch(base + "/addBook", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({ id: id, title: title, author: author, quantity: qty })
    }).then(r => r.json()).then(d => { alert(d.message); loadBooks(); });
}

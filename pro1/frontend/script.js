// Fetch all books from backend
fetch('http://localhost:8000/books')
    .then(response => response.json())
    .then(books => {
        const bookList = document.getElementById('book-list');
        books.forEach(b => {
            const li = document.createElement('li');
            li.textContent = `${b.id}: ${b.title} by ${b.author} (Qty: ${b.quantity})`;
            bookList.appendChild(li);
        });
    })
    .catch(err => console.error('Error fetching books:', err));

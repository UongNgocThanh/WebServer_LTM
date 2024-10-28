const apiUrl = 'http://172.20.10.2:8080/api/users'; // Địa chỉ API của bạn

// Function to fetch and display users
function fetchUsers() {
    fetch(apiUrl)
        .then(response => response.json())
        .then(users => {
            const tableBody = document.querySelector('#user-table tbody');
            tableBody.innerHTML = ''; // Clear existing rows
            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td class="actions">
                        <button class="edit-btn" onclick="editUser(${user.id})">Edit</button>
                        <button class="delete-btn" onclick="deleteUser(${user.id})">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching users:', error);
        });
}

// Function to add a new user or update an existing one
document.getElementById('user-form').addEventListener('submit', function(event) {
    event.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const userId = document.getElementById('user-id').value;

    if (userId) {
        // Update user if userId exists
        fetch(`${apiUrl}/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, email }),
        })
        .then(() => {
            clearForm();
            fetchUsers(); // Refresh the list
        })
        .catch(error => {
            console.error('Error updating user:', error);
        });
    } else {
        // Add new user
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, email }),
        })
        .then(() => {
            clearForm();
            fetchUsers(); // Refresh the list
        })
        .catch(error => {
            console.error('Error adding user:', error);
        });
    }
});

// Function to delete a user
function deleteUser(id) {
    fetch(`${apiUrl}/${id}`, {
        method: 'DELETE',
    })
    .then(() => fetchUsers())
    .catch(error => {
        console.error('Error deleting user:', error);
    });
}

// Function to edit a user
function editUser(id) {
    fetch(`${apiUrl}/${id}`)
        .then(response => response.json())
        .then(user => {
            document.getElementById('user-id').value = user.id;
            document.getElementById('name').value = user.name;
            document.getElementById('email').value = user.email;
//            document.getElementById('update-btn').style.display = 'inline'; // Show Update button
        })
        .catch(error => {
            console.error('Error fetching user for edit:', error);
        });
}

// Function to clear the form
function clearForm() {
    document.getElementById('user-id').value = '';
    document.getElementById('name').value = '';
    document.getElementById('email').value = '';
    document.getElementById('update-btn').style.display = 'none'; // Hide Update button
}

// Initial fetch to load users
fetchUsers();

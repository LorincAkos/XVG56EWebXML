 const STORAGE_KEY = 'hallgatok';

    let sortField = 'nev';
    let sortDir = 'asc';


    function getStudents() {
        const raw = localStorage.getItem(STORAGE_KEY);
        if (!raw) return [];
        try {
            const arr = JSON.parse(raw);
            return Array.isArray(arr) ? arr : [];
        } catch (e) {
            console.error('Hibás JSON a LocalStorage-ban', e);
            return [];
        }
    }

    function saveStudents(students) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(students));
    }


    function getFilteredAndSortedStudents() {
        let students = getStudents();
        const searchText = document.getElementById('searchText').value.trim().toLowerCase();
        const searchField = document.getElementById('searchField').value;

        // Szűrés
        if (searchText) {
            students = students.filter(s => {
                if (searchField === 'all') {
                    return Object.values(s).some(v =>
                        String(v).toLowerCase().includes(searchText)
                    );
                } else {
                    return String(s[searchField] || '')
                        .toLowerCase()
                        .includes(searchText);
                }
            });
        }

        students.sort((a, b) => {
            let aVal = a[sortField] || '';
            let bVal = b[sortField] || '';

            if (sortField === 'evfolyam') {
                aVal = parseInt(aVal, 10) || 0;
                bVal = parseInt(bVal, 10) || 0;
            } else {
                aVal = String(aVal).toLowerCase();
                bVal = String(bVal).toLowerCase();
            }

            if (aVal < bVal) return sortDir === 'asc' ? -1 : 1;
            if (aVal > bVal) return sortDir === 'asc' ? 1 : -1;
            return 0;
        });

        return students;
    }

    function renderSortButtons() {
        const sortNevBtn = document.getElementById('sortNevBtn');
        const sortEvBtn = document.getElementById('sortEvfolyamBtn');
        const sortNevInd = document.getElementById('sortNevInd');
        const sortEvInd = document.getElementById('sortEvfolyamInd');

        sortNevBtn.classList.toggle('active', sortField === 'nev');
        sortEvBtn.classList.toggle('active', sortField === 'evfolyam');

        sortNevInd.textContent = '';
        sortEvInd.textContent = '';

        const arrow = sortDir === 'asc' ? '▲' : '▼';
        if (sortField === 'nev') {
            sortNevInd.textContent = arrow;
        } else if (sortField === 'evfolyam') {
            sortEvInd.textContent = arrow;
        }
    }

    function renderTable() {
        const tbody = document.querySelector('#studentsTable tbody');
        tbody.innerHTML = '';

        const students = getFilteredAndSortedStudents();

        if (students.length === 0) {
            const tr = document.createElement('tr');
            const td = document.createElement('td');
            td.colSpan = 6;
            td.textContent = 'Nincs megjeleníthető hallgató.';
            tr.appendChild(td);
            tbody.appendChild(tr);
            return;
        }

        students.forEach((s, index) => {
            const tr = document.createElement('tr');

            const tdNev = document.createElement('td');
            tdNev.textContent = s.nev;
            tr.appendChild(tdNev);

            const tdEvfolyam = document.createElement('td');
            tdEvfolyam.textContent = s.evfolyam;
            tr.appendChild(tdEvfolyam);

            const tdSzak = document.createElement('td');
            tdSzak.textContent = s.szak;
            tr.appendChild(tdSzak);

            const tdEmail = document.createElement('td');
            tdEmail.textContent = s.email;
            tr.appendChild(tdEmail);

            const tdNeptun = document.createElement('td');
            tdNeptun.textContent = s.neptunkod;
            tr.appendChild(tdNeptun);

            const tdActions = document.createElement('td');
            tdActions.classList.add('actions');

            const editBtn = document.createElement('button');
            editBtn.textContent = 'Módosítás';
            editBtn.type = 'button';
            editBtn.addEventListener('click', () => editStudent(index));
            tdActions.appendChild(editBtn);

            const delBtn = document.createElement('button');
            delBtn.textContent = 'Törlés';
            delBtn.type = 'button';
            delBtn.classList.add('secondary');
            delBtn.addEventListener('click', () => deleteStudent(index));
            tdActions.appendChild(delBtn);

            tr.appendChild(tdActions);
            tbody.appendChild(tr);
        });

        renderSortButtons();
    }

    function resetForm() {
        document.getElementById('studentForm').reset();
        document.getElementById('editIndex').value = '';
        document.getElementById('editInfo').textContent = '';
        document.getElementById('saveBtn').textContent = 'Mentés';
    }

    function editStudent(index) {
        const students = getStudents();
        const s = students[index];
        if (!s) return;

        document.getElementById('nev').value = s.nev;
        document.getElementById('evfolyam').value = s.evfolyam;
        document.getElementById('szak').value = s.szak;
        document.getElementById('email').value = s.email;
        document.getElementById('neptunkod').value = s.neptunkod;
        document.getElementById('editIndex').value = index;
        document.getElementById('editInfo').textContent = 'Módosítás módban (sor: ' + (index + 1) + ').';
        document.getElementById('saveBtn').textContent = 'Módosítás mentése';
    }

    function deleteStudent(index) {
        if (!confirm('Biztosan törli ezt a hallgatót?')) return;
        const students = getStudents();
        students.splice(index, 1);
        saveStudents(students);
        resetForm();
        renderTable();
    }

    function exportJSON() {
        const students = getStudents();
        const dataStr = JSON.stringify(students, null, 2);
        const blob = new Blob([dataStr], { type: 'application/json' });
        const url = URL.createObjectURL(blob);

        const a = document.createElement('a');
        a.href = url;
        a.download = 'hallgatok.json';
        document.body.appendChild(a);
        a.click();

        document.body.removeChild(a);
        URL.revokeObjectURL(url);
    }

    function importJSON(file) {
        const reader = new FileReader();
        reader.onload = function (event) {
            try {
                const data = JSON.parse(event.target.result);
                if (!Array.isArray(data)) {
                    alert('A JSON fájl nem tömböt tartalmaz.');
                    return;
                }
                // nagyon egyszerű validáció
                const valid = data.every(item =>
                    typeof item.nev !== 'undefined' &&
                    typeof item.evfolyam !== 'undefined' &&
                    typeof item.szak !== 'undefined' &&
                    typeof item.email !== 'undefined' &&
                    typeof item.neptunkod !== 'undefined'
                );
                if (!valid) {
                    alert('A JSON formátum nem megfelelő (hiányzó mezők).');
                    return;
                }
                saveStudents(data);
                resetForm();
                renderTable();
                alert('Import sikeres. A lista frissült.');
            } catch (e) {
                console.error(e);
                alert('Hibás JSON fájl.');
            }
        };
        reader.readAsText(file, 'utf-8');
    }

    document.addEventListener('DOMContentLoaded', () => {
        const form = document.getElementById('studentForm');
        const resetBtn = document.getElementById('resetBtn');
        const searchText = document.getElementById('searchText');
        const searchField = document.getElementById('searchField');
        const sortButtons = document.querySelectorAll('.sort-btn');
        const exportBtn = document.getElementById('exportBtn');
        const importBtn = document.getElementById('importBtn');
        const importFile = document.getElementById('importFile');

        form.addEventListener('submit', (e) => {
            e.preventDefault();

            const nev = document.getElementById('nev').value.trim();
            const evfolyam = document.getElementById('evfolyam').value.trim();
            const szak = document.getElementById('szak').value.trim();
            const email = document.getElementById('email').value.trim();
            const neptunkod = document.getElementById('neptunkod').value.trim();

            if (!nev || !evfolyam || !szak || !email || !neptunkod) {
                alert('Minden mező kitöltése kötelező!');
                return;
            }

            const students = getStudents();
            const editIndex = document.getElementById('editIndex').value;

            const newStudent = { nev, evfolyam, szak, email, neptunkod };

            if (editIndex === '') {
                students.push(newStudent);
            } else {
                students[parseInt(editIndex, 10)] = newStudent;
            }

            saveStudents(students);
            resetForm();
            renderTable();
        });

        resetBtn.addEventListener('click', () => {
            resetForm();
        });

        searchText.addEventListener('input', renderTable);
        searchField.addEventListener('change', renderTable);

        sortButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const field = btn.getAttribute('data-field');
                if (sortField === field) {
                    sortDir = sortDir === 'asc' ? 'desc' : 'asc';
                } else {
                    sortField = field;
                    sortDir = 'asc';
                }
                renderTable();
            });
        });

        exportBtn.addEventListener('click', exportJSON);

        importBtn.addEventListener('click', () => {
            const file = importFile.files[0];
            if (!file) {
                alert('Válassz egy JSON fájlt importáláshoz!');
                return;
            }
            importJSON(file);
        });

        renderTable();
    });

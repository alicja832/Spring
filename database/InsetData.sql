create schema ap_python;

SET search_path to ap_python;

CREATE TABLE solution (
    id SERIAL,
    solution_content TEXT NOT NULL,
    score INTEGER DEFAULT 0,
    output TEXT DEFAULT NULL,
    exercise_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    CONSTRAINT solutions_pk PRIMARY KEY (id),
    CONSTRAINT solutions_exercises_fk FOREIGN KEY(exercise_id) REFERENCES exercise(id) ON DELETE CASCADE,
    CONSTRAINT solutions_students_fk FOREIGN KEY(student_id) REFERENCES student(id) ON DELETE CASCADE
);

CREATE TABLE exercise (
    id SERIAL,
    name VARCHAR NOT NULL,
    introduction TEXT NOT NULL,
    content TEXT NOT NULL,
    max_points INTEGER NOT NULL,
    solution TEXT NOT NULL,
    output TEXT NOT NULL,
    teacher_id INTEGER NOT NULL,
    UNIQUE (name),
    CONSTRAINT exercises_pk PRIMARY KEY (id),
    CONSTRAINT solutions_students_fk FOREIGN KEY(teacher_id) REFERENCES teacher(id) ON DELETE CASCADE
);

CREATE TABLE student (
    id SERIAL,
    score INTEGER,
    user_id INTEGER NOT NULL,
    CONSTRAINT student_pk PRIMARY KEY (id),
    CONSTRAINT users_students_fk FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE teacher (
    id SERIAL,
    user_id INTEGER NOT NULL,
    CONSTRAINT teacher_pk PRIMARY KEY (id),
    CONSTRAINT users_teacher_fk FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE users (
    id SERIAL,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    UNIQUE (name),
    UNIQUE (email),
    CONSTRAINT user_pk PRIMARY KEY (id)
);

INSERT INTO users(name,email,password) VALUES ('Anna','anna@gmail.com','$2a$12$ErkIeyihwOe1ItpMEY//feNhVsjVDvtsMTgMN7bCAHwPV8.aApMpO');
INSERT INTO teacher(user_id) VALUES (1);
INSERT INTO exercise(name,introduction,content,max_points,solution,output,teacher_id)VALUES
(
    'Algorytm BFS',
    'BFS (ang. Breadth-first search) to drugi, zupełnie inny sposób przeszukiwania grafu. Do realizacji tego algorytmu wykorzystujemy kolejkę fifo. Pokażemy działanie tego algorytmu na przykładzie: Mamy daną listę sąsiedztwa grafu: (listę poszczególnych wierzchołków wraz z numerami sąsiadujących z nimi wierzchołków)
    (1)-> [2,5,9] \n
    (2)-> [1,4,3] \n
    (3)-> [2,4] \n
    (4)-> [2,3] \n
    (5)-> [7,8,6] \n
    (6)-> [5] \n
    (7)-> [5] \n,
    (8)-> [5,10] \n,
    (9)-> [1,10] \n,
    (10)-> [9,8] \n
    Załóżmy, że zaczynamy przeszukiwanie od wierzchołka numer 1. Wrzucamy go do kolejki i postępujemy z zasadą:
wrzucamy do kolejki wszystkie wierzchołki, które są z nim połączone. Kolejka teraz wygląda następująco:1,2,5,9
następnie opuszczamy wierzchołek numer 1 usuwając go jednocześnie z kolejki: 2,5,9
teraz pobieramy następny element z kolejki - jest to wierzchołek numer 2
dodajemy wszystkie nieodwiedzone połączenia tego wierzchołka do kolejki, a więc wierzchołek 3 i 4:
2,5,9,3,4
wychodzimy z wierzchołka 2 usuwając go z kolejki i pobieramy następny jej element: 5 oraz wrzucamy do kolejki wszystkie nieodwiedzone połączenia z tym wierzchołkiem: 5,9
3,4,6,7,8
czynności te powtarzamy do momentu przejścia całego grafu:
Odwiedzenie wszystkich wierzchołków zakończy się w momencie, gdy kolejka będzie pusta. Przejście przez cały graf (odwiedzenie wszystkich wierzchołków) ma złożoność liniową, a dokładnie
O(n+p)',
'Zaimplementuj przeszukiwanie wszerz w języku Python wykorzystując podaną w przykładzie listę sąsiedztwa. Twoim zadaniem jest wypisywanie aktualnie odwiedzonych wierzchołków oraz '


)adjacency_list = {1: [2,5,9],
    2: [1,3,4],
    3: [2,4],
    4: [2,3],
    5: [6,7,8],
    6: [5],
    7: [5],
    8: [5,10],
    9: [1,10],
    10: [8,9]}
queque = []
queque.append(1)
visited = []

while (queque):
    visit = queque.pop(0)
    print(f'Odwiedzono wierzchołek {visit}')
    for node in adjacency_list[visit]:
        if node not in visited:
            queque.append(node)
    visited.append(visit)
    print(queque)
print(visited)
Zaimplementuj przeszukiwanie wszerz w języku Python wykorzystując podaną w przykładzie listę sąsiedztwa. Twoim zadaniem jest wypisywanie aktualnie odwiedzonanych wierzchołków oraz aktualnej zawartości kolejki. 




adjacency_list = {1: [2,5,9],
    2: [1,3,4],
    3: [2,4],
    4: [2,3],
    5: [6,7,8],
    6: [5],
    7: [5],
    8: [5,10],
    9: [1,10],
    10: [8,9]}
queque = []
queque.append(1)
visited = []

while (queque):
    visit = queque.pop(0)
    print("Odwiedzono wierzcholek "+str(visit))
    for node in adjacency_list[visit]:
        if node not in visited and node not in queque:
            queque.append(node)
    visited.append(visit)
    print(queque)




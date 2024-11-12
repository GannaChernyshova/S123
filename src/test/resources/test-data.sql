DELETE
FROM todos;

insert into todos (id, title, completed, order_number)
values ('1', 'Buy groceries', false, 1),
       ('2', 'Prepare presentation', true, 2),
       ('3', 'Finish SQL script', false, 3),
       ('4', 'Book flight tickets', false, 4),
       ('5', 'Write blog post', true, 5)
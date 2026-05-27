const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
const unit = 20;
const rows = canvas.height / unit;
const cols = canvas.width / unit;

let snake = [{ x: 10, y: 10 }];
let direction = { x: 1, y: 0 };
let food = randomFood();
let score = 0;
let running = true;
let speed = 120;

function randomFood() {
    return {
        x: Math.floor(Math.random() * cols),
        y: Math.floor(Math.random() * rows)
    };
}

function draw() {
    ctx.fillStyle = '#0b0b0b';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = '#ff4d4d';
    ctx.fillRect(food.x * unit, food.y * unit, unit, unit);

    for (let i = 0; i < snake.length; i++) {
        ctx.fillStyle = i === 0 ? '#2ecc71' : '#10a37f';
        ctx.fillRect(snake[i].x * unit, snake[i].y * unit, unit - 2, unit - 2);
    }

    ctx.fillStyle = '#fff';
    ctx.font = '18px Arial';
    ctx.fillText('Score: ' + score, 10, 24);

    if (!running) {
        ctx.fillStyle = 'rgba(0, 0, 0, 0.75)';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = '#fff';
        ctx.font = '36px Arial';
        ctx.fillText('Game Over', canvas.width / 2 - 100, canvas.height / 2 - 20);
        ctx.font = '20px Arial';
        ctx.fillText('Press Space to Restart', canvas.width / 2 - 125, canvas.height / 2 + 20);
    }
}

function update() {
    if (!running) return;

    const head = { x: snake[0].x + direction.x, y: snake[0].y + direction.y };

    if (head.x < 0 || head.x >= cols || head.y < 0 || head.y >= rows || snake.some(part => part.x === head.x && part.y === head.y)) {
        running = false;
        return;
    }

    snake.unshift(head);

    if (head.x === food.x && head.y === food.y) {
        score += 10;
        food = randomFood();
    } else {
        snake.pop();
    }
}

function gameLoop() {
    update();
    draw();
}

window.addEventListener('keydown', event => {
    switch (event.key) {
        case 'ArrowUp':
        case 'w':
        case 'W':
            if (direction.y === 0) direction = { x: 0, y: -1 };
            break;
        case 'ArrowDown':
        case 's':
        case 'S':
            if (direction.y === 0) direction = { x: 0, y: 1 };
            break;
        case 'ArrowLeft':
        case 'a':
        case 'A':
            if (direction.x === 0) direction = { x: -1, y: 0 };
            break;
        case 'ArrowRight':
        case 'd':
        case 'D':
            if (direction.x === 0) direction = { x: 1, y: 0 };
            break;
        case ' ':
            if (!running) {
                snake = [{ x: 10, y: 10 }];
                direction = { x: 1, y: 0 };
                food = randomFood();
                score = 0;
                running = true;
            }
            break;
    }
});

setInterval(gameLoop, speed);

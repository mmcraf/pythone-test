const canvas = document.createElement('canvas');
const ctx = canvas.getContext('2d');
canvas.width = 800;
canvas.height = 600;
document.body.appendChild(canvas);

// Game variables
const player1 = { x: 100, y: 500, width: 50, height: 50, color: 'blue', bullets: [] };
const player2 = { x: 600, y: 500, width: 50, height: 50, color: 'red', bullets: [] };
const fallingSquares = [];
const keys = {};

// Key bindings
const controls = {
  player1: { left: 'a', right: 'd', shoot: 'w' },
  player2: { left: 'ArrowLeft', right: 'ArrowRight', shoot: 'ArrowUp' }
};

// Event listeners for key presses
document.addEventListener('keydown', (e) => {
  keys[e.key] = true;
});

document.addEventListener('keyup', (e) => {
  keys[e.key] = false;
});

// Function to spawn falling squares
function spawnFallingSquare() {
  const size = 30;
  fallingSquares.push({
    x: Math.random() * (canvas.width - size),
    y: -size,
    size: size,
    speed: 2 + Math.random() * 3
  });
}

// Function to draw rectangles
function drawRect(obj) {
  ctx.fillStyle = obj.color;
  ctx.fillRect(obj.x, obj.y, obj.width, obj.height);
}

// Function to draw bullets
function drawBullets(bullets, color) {
  ctx.fillStyle = color;
  bullets.forEach((bullet) => {
    ctx.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
  });
}

// Function to update bullets
function updateBullets(bullets, speed) {
  bullets.forEach((bullet, index) => {
    bullet.y -= speed;
    if (bullet.y < 0) bullets.splice(index, 1);
  });
}

// Main game loop
function gameLoop() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // Move players
  if (keys[controls.player1.left] && player1.x > 0) player1.x -= 5;
  if (keys[controls.player1.right] && player1.x + player1.width < canvas.width) player1.x += 5;
  if (keys[controls.player2.left] && player2.x > 0) player2.x -= 5;
  if (keys[controls.player2.right] && player2.x + player2.width < canvas.width) player2.x += 5;

  // Shooting
  if (keys[controls.player1.shoot]) {
    player1.bullets.push({ x: player1.x + player1.width / 2 - 5, y: player1.y, width: 10, height: 20 });
    keys[controls.player1.shoot] = false; // Prevent holding to shoot
  }
  if (keys[controls.player2.shoot]) {
    player2.bullets.push({ x: player2.x + player2.width / 2 - 5, y: player2.y, width: 10, height: 20 });
    keys[controls.player2.shoot] = false;
  }

  // Update bullets
  updateBullets(player1.bullets, 10);
  updateBullets(player2.bullets, 10);

  // Update falling squares
  fallingSquares.forEach((square, index) => {
    square.y += square.speed;
    if (square.y > canvas.height) fallingSquares.splice(index, 1);
  });

  // Draw players, bullets, and squares
  drawRect(player1);
  drawRect(player2);
  drawBullets(player1.bullets, 'blue');
  drawBullets(player2.bullets, 'red');

  fallingSquares.forEach((square) => {
    ctx.fillStyle = 'green';
    ctx.fillRect(square.x, square.y, square.size, square.size);
  });

  requestAnimationFrame(gameLoop);
}

// Start spawning squares
setInterval(spawnFallingSquare, 1000);

// Start the game loop
gameLoop();

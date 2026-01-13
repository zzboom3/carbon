const fs = require('fs');
const path = require('path');
const glob = require('glob');

// Use glob to find all .vue files in src
// Note: glob package might not be installed in the root context, but usually is available or I can use a recursive walk.
// Since I can't rely on 'glob' package being installed, I'll write a simple recursive walker.

function walkDir(dir, callback) {
  fs.readdirSync(dir).forEach(f => {
    let dirPath = path.join(dir, f);
    let isDirectory = fs.statSync(dirPath).isDirectory();
    if (isDirectory) {
      walkDir(dirPath, callback);
    } else {
      callback(path.join(dir, f));
    }
  });
}

const rootDir = path.resolve('src');

walkDir(rootDir, (filePath) => {
  if (filePath.endsWith('.vue')) {
    const content = fs.readFileSync(filePath, 'utf8');
    if (content.includes('/deep/')) {
      const newContent = content.replace(/\/deep\//g, '::v-deep');
      fs.writeFileSync(filePath, newContent, 'utf8');
      console.log(`Updated: ${filePath}`);
    }
  }
});

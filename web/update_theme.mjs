import fs from 'fs';
import path from 'path';

const srcDir = 'c:/Users/vinay/OneDrive/Desktop/RoomShare/web/src';

function walk(dir) {
  let results = [];
  const list = fs.readdirSync(dir);
  list.forEach(file => {
    file = path.join(dir, file);
    const stat = fs.statSync(file);
    if (stat && stat.isDirectory()) {
      results = results.concat(walk(file));
    } else {
      if (file.match(/\.(jsx?|tsx?)$/)) {
        results.push(file);
      }
    }
  });
  return results;
}

const files = walk(srcDir);

files.forEach(file => {
  let content = fs.readFileSync(file, 'utf8');
  let newContent = content
    // Replace all massive rem-based rounding to rounded-xl
    .replace(/rounded-\[([0-9.]+)rem\]/g, 'rounded-xl')
    // Replace pixel based huge rounding
    .replace(/rounded-\[[2-9][0-9]+px\]/g, 'rounded-xl')
    .replace(/rounded-3xl/g, 'rounded-xl')
    .replace(/rounded-2xl/g, 'rounded-xl')
    // Update theme shadow to look more premium/modern
    .replace(/shadow-sm/g, 'shadow-md')
    // Make borders slightly more subtle
    .replace(/border-border(?!\/)/g, 'border-border/60');
    
  if (content !== newContent) {
    fs.writeFileSync(file, newContent, 'utf8');
    console.log('Updated:', file);
  }
});

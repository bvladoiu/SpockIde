// puppeteerScript.js
const puppeteer = require('puppeteer');

(async () => {
  try {
    const browser = await puppeteer.launch({
      headless: false,  
      args: [
        '--disable-web-security',
        '--disable-features=IsolateOrigins,site-per-process',
//        '--no-sandbox',
         '--auto-open-devtools-for-tabs',
         '--remote-debugging-port=9222',
      ]
    });
    const page = await browser.newPage();
    await page.goto('about:blank');  

    console.log('Chromium launched with security disabled, and a blank page loaded.');

  } catch (err) {
    console.error('Error launching Puppeteer:', err);
  }
})();

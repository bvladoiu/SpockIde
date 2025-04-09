const { webkit } = require('playwright');

(async () => {
  const browser = await webkit.launch({ headless: false }); // Launch in headed (non-headless) mode
  const page = await browser.newPage();
  await page.goto('https://www.example.com');
  // Your automation code here...
  //await browser.close();
})();

//  npx onchange "server-jvm/src/**/*.kt" -- ./gradlew :server-jvm:run

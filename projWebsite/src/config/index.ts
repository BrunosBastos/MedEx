export let MEDEX_API_BASE_URL: string = null;
if (process.env.REACT_APP_RUNNING_MODE != null && process.env.REACT_APP_RUNNING_MODE == 'PRODUCTION'){
    let HOST = "192.168.160.231"
    console.log(MEDEX_API_BASE_URL);
    MEDEX_API_BASE_URL = `https://${HOST}:8080/api/v1/`;
}
else {
    MEDEX_API_BASE_URL = 'http://localhost:8080/api/v1/';
}
import {MEDEX_API_BASE_URL, TEST_TOKEN} from '../config/index';
class SupplierService {
    getSuppliers() {
        return fetch( MEDEX_API_BASE_URL + "suppliers", {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ TEST_TOKEN
            },
        })
    }
}
export default new SupplierService()
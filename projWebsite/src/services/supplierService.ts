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

    addNewSupplier(name: string, lat: number, lon: number) {
        return fetch( MEDEX_API_BASE_URL + "suppliers", {
            method: 'Post',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ TEST_TOKEN
            },
            body: JSON.stringify({name: name, lat: lat, lon: lon})
        })
    }
}
export default new SupplierService()
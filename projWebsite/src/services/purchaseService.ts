import { MEDEX_API_BASE_URL } from '../config/index';
import useAuthStore from 'src/stores/useAuthStore';

class ProductService {

    makePurchase(lat: number, lon: number, products: any) {
        return fetch(MEDEX_API_BASE_URL + 'purchases', {
            method: 'Post',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            },
            body: JSON.stringify({
                lat: lat, 
                lon: lon, 
                products: products
            })
        })
    }

    getPurchaseDetails(id: any) {
        return fetch(MEDEX_API_BASE_URL + 'purchases/' + id, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }
}

export default new ProductService();
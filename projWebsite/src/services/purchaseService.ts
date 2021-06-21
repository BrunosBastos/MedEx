import { MEDEX_API_BASE_URL } from '../config/index';
import useAuthStore from 'src/stores/useAuthStore';

class PurchaseService {

    getPurchases(page, recent){
        return fetch(MEDEX_API_BASE_URL + 'purchases?page='+page+'&recent='+recent, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            }
        })
    }

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

    makeReview(id, desc, rating) {
        return fetch(MEDEX_API_BASE_URL + 'purchases/' + id, {
            method: 'POST',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            },
            body: JSON.stringify({
                desc: desc,
                rating: rating, 
            })
        })
    }
}

export default new PurchaseService();
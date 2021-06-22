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
    makeReview(host, id, desc, rating) {
        return fetch(MEDEX_API_BASE_URL + 'reviews', {
            method: 'POST',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ useAuthStore.getState().token
            },
            body: JSON.stringify({
                host: host,
                purchaseId: id,
                comment: desc,
                rating: rating, 
            })
        })
    }
}

export default new PurchaseService();
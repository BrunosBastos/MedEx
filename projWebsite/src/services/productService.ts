import {MEDEX_API_BASE_URL, TEST_TOKEN} from '../config/index';

class ProductService {


    getAllProducts(){
        return fetch(MEDEX_API_BASE_URL + 'products', {
            method: 'GET',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ TEST_TOKEN
            }
        })
    }

    addnewProduct(name:string, description: string,address:string, price: number, stock: number, photo: string, supplier: number ){

        return fetch(MEDEX_API_BASE_URL + 'products', {
            method: 'POST',
            mode: 'cors',
            headers: {
                'accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization' : "Bearer "+ TEST_TOKEN
            },
            body: JSON.stringify({
                name: name, 
                description: description, 
                address: address,
                price: price, 
                stock: stock, 
                photo:photo, 
                supplier: supplier})
        })
    }
}

export default new ProductService();
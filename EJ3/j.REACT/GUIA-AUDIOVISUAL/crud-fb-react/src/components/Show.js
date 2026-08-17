import React, {useState, useEffect} from "react";
import { Link } from "react-router-dom";
import { collection, getDocs, getDoc, deleteDoc, doc } from "firebase/firestore";
import { db } from "../firebaseConfig/firebase";
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const MySwal = withReactContent(Swal)

const Show = () => {
    //1 - Configuramos los hooks
    const [products, setProducts] = useState([])
    //2 - Referenciamos a la db de firestone
    const productsCollection = collection(db, "products")

    //3 - funcion para mostrar TODOS los docs
    const getProducts = async () => {
        const data = await getDocs(productsCollection)
    
    setProducts(
        data.docs.map( (doc) => ({...doc.data(), id:doc.id}))

    )
    console.log(products)
    }
    //4 - funcion para eliminar un doc
    const deleteProduct = async (id) => {
        const productDoc = doc(db, "products", id)
        await deleteDoc(productDoc)
        getProducts()
    }
    //5 - funcion de configuracion para sweet alert 2

    //6 usamos useEffect
    useEffect( () => {
        getProducts()
    }, [])
    //7 - devolvemos vista de nuestro componente
    return (
        <>
        <div className="container">
            <div className="row">
                <div className="col">
                    <div className="d-grip gap-2">
                    <Link to="/create" className="btn btn-secondary mt-2 mb-2">Create</Link>
                    </div>

                    <table className="table table-dark table-hover">
                        <thead>
                            <tr>
                                <th>Description</th>
                                <th>Stock</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            {products.map( (product) => (
                                <tr key={product.id}>
                                    <td>{product.description}</td>
                                    <td>{product.stock}</td>
                                    <td>
                                        <Link to={`/edit/${product.id}`}><i className="fa fa-pencil" aria-hidden="true"></i></Link>
                                        <button onClick={() => deleteProduct(product.id)} className="btn btn-danger"><i className="fa fa-trash" aria-hidden="true"></i></button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </>
        
    )
}

export default Show
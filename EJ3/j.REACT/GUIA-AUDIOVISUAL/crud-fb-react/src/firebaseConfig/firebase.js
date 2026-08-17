// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyBBU0u6DFfRiOLBZQZgdZWdhbZ37S9KI08",
  authDomain: "crud-fire-react-b4a3d.firebaseapp.com",
  projectId: "crud-fire-react-b4a3d",
  storageBucket: "crud-fire-react-b4a3d.firebasestorage.app",
  messagingSenderId: "351633322446",
  appId: "1:351633322446:web:c9719cc2306832c5997b9f",
  measurementId: "G-PQWENXL264"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

//Conexión a BD
export const db = getFirestore(app);
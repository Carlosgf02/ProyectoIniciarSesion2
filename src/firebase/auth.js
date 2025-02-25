import { signInWithPopup } from 'firebase/auth';
import { auth, googleProvider } from './config';

export const signInWithGoogle = async() => {
    try {
        const result = await signInWithPopup(auth, googleProvider);
        return result.user;
    } catch (error) {
        console.error("Error al iniciar sesión con Google:", error);
        throw error;
    }
};
import { signInWithGoogle } from '../firebase/auth';

function Login() {
    const handleGoogleSignIn = async() => {
        try {
            await signInWithGoogle();
            // Redirigir al usuario o actualizar el estado según sea necesario
        } catch (error) {
            console.error("Error en inicio de sesión con Google:", error);
            // Manejar el error apropiadamente
        }
    };

    return ( <
        div >
        <
        button onClick = { handleGoogleSignIn }
        className = "w-full bg-white text-gray-700 border border-gray-300 rounded-lg px-4 py-2 mt-4 flex items-center justify-center gap-2" >
        <
        img src = "/google-icon.svg"
        alt = "Google"
        className = "w-5 h-5" / >
        Iniciar sesión con Google <
        /button> <
        /div>
    );
}

export default Login;
TextButton(
    onClick = { 
        navController.navigate("forgot_password")
    },
    modifier = Modifier.fillMaxWidth()
) {
    Text("¿Olvidaste tu contraseña?")
} 
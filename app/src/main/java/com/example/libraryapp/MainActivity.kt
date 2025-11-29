package com.example.libraryapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.libraryapp.ui.theme.GreenJC

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = BookRepository(RetrofitInstance.api)
        val viewModel: BookViewModel by viewModels { BookViewModelFactory(repository) }

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "bookList") {
                composable("bookList") { BookListScreen(viewModel, navController) }
                composable("addBook") { AddBookScreen(viewModel, navController) }
                composable("bookDetail/{bookId}") { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId")
                    val book = viewModel.allBooks.observeAsState(initial = emptyList()).value.find { it.id == bookId }
                    book?.let { BookDetailScreen(it, viewModel, navController) }
                }
                composable("editBook/{bookId}") { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId")
                    val book = viewModel.allBooks.observeAsState(initial = emptyList()).value.find { it.id == bookId }
                    book?.let { EditBookScreen(it, viewModel, navController) }
                }
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(book.imagem),
                contentDescription = book.titulo,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(book.titulo)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(viewModel: BookViewModel, navController: NavController) {
    val context = LocalContext.current.applicationContext

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)) {
                        Text("Livros", fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { Toast.makeText(context, "Livros", Toast.LENGTH_SHORT).show() }) {
                        Icon(painter = painterResource(id = R.drawable.books), contentDescription = null)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = GreenJC,
                onClick = { navController.navigate("addBook") }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Livro")
            }
        }
    ) { paddingValues ->
        val books by viewModel.allBooks.observeAsState(initial = emptyList())
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(books) { book ->
                BookItem(book = book) {
                    navController.navigate("bookDetail/${book.id}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(viewModel: BookViewModel, navController: NavController) {
    var imagem by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var editora by remember { mutableStateOf("") }
    var anoPublicacao by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = { Text(text = "Adicionar livro", fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White
                )
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = imagem, onValueChange = { imagem = it }, label = { Text(text = "URL da Imagem") }, modifier = Modifier.fillMaxWidth())
            TextField(value = titulo, onValueChange = { titulo = it }, label = { Text(text = "Título") }, modifier = Modifier.fillMaxWidth())
            TextField(value = isbn, onValueChange = { isbn = it }, label = { Text(text = "ISBN") }, modifier = Modifier.fillMaxWidth())
            TextField(value = autor, onValueChange = { autor = it }, label = { Text(text = "Autor") }, modifier = Modifier.fillMaxWidth())
            TextField(value = editora, onValueChange = { editora = it }, label = { Text(text = "Editora") }, modifier = Modifier.fillMaxWidth())
            TextField(value = anoPublicacao, onValueChange = { anoPublicacao = it }, label = { Text(text = "Ano de publicação") }, modifier = Modifier.fillMaxWidth())
            TextField(value = genero, onValueChange = { genero = it }, label = { Text(text = "Gênero") }, modifier = Modifier.fillMaxWidth())
            TextField(value = preco, onValueChange = { preco = it }, label = { Text(text = "Preço") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                viewModel.addBook(imagem, titulo, isbn, autor, editora, anoPublicacao, genero, preco)
                navController.navigate("bookList") { popUpTo(0) }
            }, colors = ButtonDefaults.buttonColors(GreenJC)) {
                Text(text = "Adicionar Livro")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(book: Book, viewModel: BookViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = { Text(text = "Detalhes do livro", fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenJC, titleContentColor = Color.White)
            )
        }, floatingActionButton = {
            FloatingActionButton(
                containerColor = GreenJC,
                onClick = { navController.navigate("editBook/${book.id}") }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar livro")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = rememberAsyncImagePainter(book.imagem), contentDescription = book.titulo, modifier = Modifier.size(128.dp).clip(CircleShape), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Título: ${book.titulo}", fontWeight = FontWeight.Bold)
            Text("ISBN: ${book.isbn}")
            Text("Autor: ${book.autor}")
            Text("Editora: ${book.editora}")
            Text("Ano de Publicação: ${book.anoPublicacao}")
            Text("Gênero: ${book.genero}")
            Text("Preço: ${book.preco}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(colors = ButtonDefaults.buttonColors(GreenJC), onClick = {
                viewModel.deleteBook(book)
                navController.navigate("bookList") { popUpTo(0) }
            }) {
                Text("Deletar livro")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(book: Book, viewModel: BookViewModel, navController: NavController) {
    var imagem by remember { mutableStateOf(book.imagem) }
    var titulo by remember { mutableStateOf(book.titulo) }
    var isbn by remember { mutableStateOf(book.isbn) }
    var autor by remember { mutableStateOf(book.autor) }
    var editora by remember { mutableStateOf(book.editora) }
    var anoPublicacao by remember { mutableStateOf(book.anoPublicacao.toString()) }
    var genero by remember { mutableStateOf(book.genero) }
    var preco by remember { mutableStateOf(book.preco) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = { Text(text = "Editar livro", fontSize = 18.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenJC, titleContentColor = Color.White)
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = rememberAsyncImagePainter(imagem), contentDescription = null, modifier = Modifier.size(128.dp).clip(CircleShape), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.height(12.dp))
            TextField(value = imagem, onValueChange = { imagem = it }, label = { Text(text = "URL da Imagem") }, modifier = Modifier.fillMaxWidth())
            TextField(value = titulo, onValueChange = { titulo = it }, label = { Text(text = "Título") }, modifier = Modifier.fillMaxWidth())
            TextField(value = isbn, onValueChange = { isbn = it }, label = { Text(text = "ISBN") }, modifier = Modifier.fillMaxWidth())
            TextField(value = autor, onValueChange = { autor = it }, label = { Text(text = "Autor") }, modifier = Modifier.fillMaxWidth())
            TextField(value = editora, onValueChange = { editora = it }, label = { Text(text = "Editora") }, modifier = Modifier.fillMaxWidth())
            TextField(value = anoPublicacao, onValueChange = { anoPublicacao = it }, label = { Text(text = "Ano de publicação") }, modifier = Modifier.fillMaxWidth())
            TextField(value = genero, onValueChange = { genero = it }, label = { Text(text = "Gênero") }, modifier = Modifier.fillMaxWidth())
            TextField(value = preco, onValueChange = { preco = it }, label = { Text(text = "Preço") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val anoInt = anoPublicacao.toIntOrNull() ?: book.anoPublicacao
                val updatedBook = book.copy(imagem = imagem, titulo = titulo, isbn = isbn, autor = autor, editora = editora, anoPublicacao = anoInt, genero = genero, preco = preco)
                viewModel.updateBook(updatedBook)
                navController.navigate("bookList") { popUpTo(0) }
            }, colors = ButtonDefaults.buttonColors(GreenJC)) {
                Text(text = "Atualizar Livro")
            }
        }
    }
}
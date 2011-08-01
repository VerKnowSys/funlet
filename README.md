# funlet - functional servlet (or call it "Web Framework")

## Why?

* simple, all you need is just `PartialFunction[Request, Response]`
* no magic, no dsl, full power of scala
* routing based on pattern matching (Why to create custom routers, just check request parameters!)
* simple templating based on scalate
* powerful forms library (define form at one place, it can display itself)
* as immutable as I could get with javax.servlet API
* not based on any ORM


It's basicly MVC, but without Model and Controllers called Endpoints.

## Minimal example
```scala
import com.verknowsys.funlet._

class Main extends MainEndpoint {
    override def routes(implicit request: Request) = {
        case Get(Nil) => <h1>Hello world</h1>
        case Post("foo" :: "bar" :: Nil) => "Hello from /foo/bar"
    }
}
```

# Composition of endpoints

```scala
object Endpoint1 extends Endpoint {
    def routes(implicit request: Request) = {
        case Get("foo" :: Nil) => <h1>Hello foo</h1>
        case Get("render" :: Nil) => render("path/to/template")
    }
}

object Endpoint2 extends Endpoint {
    def routes(implicit request: Request) = {
        case Get("redirect" :: Nil) => redirect("/", session = ("key" -> "value"))
        case Get("foos" :: id :: Nil) => render("foos/show", "foo" -> getFromFromSomewhere(id))
    }
}

class Main extends MainEndpoint {
    override val endpoints = Endpoint1 :: Endpoint2 :: Nil
}
````

Then add your `Main` class as servlet in `web.xml` config file.


## Forms

For `User` class like:

```scala
case class User(name: String, age: Int)
```

Form definition:

```scala
class UserForm(entity: Option[User] = None, param: Param = Empty) extends Form[User](entity, param) {
    // extract values from fields (if provided and valid)
    def bind = for {
        n <- name
        a <- age
    } yield (entity.map { _.copy _ } getOrElse User.apply _)(n, a) // use case class copy method or create new object

    // (name of field, getter, validators)
    val name = new StringField("name", _.name, NotEmpty)
    val age = new IntField("age", _.age, LessThan(100))

    // list of fields
    def fields = name :: age :: Nil
}
```

Endpoint:

```scala
object UserEndpoint extends Endpoint {
    def routes(implicit request: Request) = {
        case Get("users" :: "new" :: Nil) =>
            render("users/new", "form" -> new UserForm())

        case Post("users" :: Nil) =>
            val form = new UserForm(param = formParam)
            if(form.isValid){
                doSomethingWith(form.get) // form.get will return User object
                redirect("users" :: Nil)
            } else {
                render("users/new", "form" -> form)
            }
    }
}
```

View:

```haml
-@ val form: BaseForm // define values for template, staticly checked

%h3 New user
= form.toHtml

```
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WallServiceTest {

    @Before
    fun clearBeforeTest() {

        WallService.clear()
    }

    @Test
    fun addPost() {

        val postBeforeAdd1: Posts<Any?> = Posts(ownerId = 1)
        val postBeforeAdd2: Posts<Any?> = Posts(ownerId = 2)
        val postBeforeAdd3: Posts<Any?> = Posts(ownerId = 3)

        val post1: Posts<Any?> = WallService.add(postBeforeAdd1)
        val post2: Posts<Any?> = WallService.add(postBeforeAdd2)
        val post3: Posts<Any?> = WallService.add(postBeforeAdd3)

        assertEquals(1, post1.id)
        assertEquals(2, post2.id)
        assertEquals(3, post3.id)
    }

    @Test
    fun updateExistingFalse() {
        val postBeforeAdd1: Posts<Any?> = Posts(ownerId = 1)
        val postBeforeAdd2: Posts<Any?> = Posts(ownerId = 2)
        val postBeforeAdd3: Posts<Any?> = Posts(ownerId = 3)

        val post1: Posts<Any?> = WallService.add(postBeforeAdd1)
        val post2: Posts<Any?> = WallService.add(postBeforeAdd2)
        val post3: Posts<Any?> = WallService.add(postBeforeAdd3)

        val postUpdate: Posts<Any?> = Posts(id = 4, ownerId = 4, text = "Update Record")
        val isUpdate = WallService.update(postUpdate)
        assertFalse(isUpdate)
    }

    @Test
    fun updateExistingTrue() {
        val postBeforeAdd = Posts<Any?>(ownerId = 1)
        val postBeforeUpdate = WallService.add(postBeforeAdd.copy())
        postBeforeUpdate.text = "Update Record"
        val isUpdate = WallService.update(postBeforeUpdate.copy())

        println(isUpdate)
        assertTrue(isUpdate)
    }
    @Test
    fun shouldAddCommentToExistingPost() {
        val wallService = WallService
        val post = Posts<Any?> (id = 1, ownerId = 1)
        wallService.add(post)

        val comment = WallService.Comment(id = 1, postId = 1, fromId = 2, text = "Test comment", date = System.currentTimeMillis())
        val result = wallService.createComment(1, comment)

        assertEquals(comment, result)
        assertEquals(1, wallService.comments.size)
        assertEquals(comment, wallService.comments[0])
    }

    @Test(expected = PostNotFoundException::class)
    fun shouldThrowExceptionForNonexistentPost() {
        val wallService = WallService
        val comment = WallService.Comment(id = 1, postId = 1, fromId = 2, text = "Test comment", date = System.currentTimeMillis())
        wallService.createComment(1, comment)
    }
}
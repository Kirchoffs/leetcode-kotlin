class ListNode(var `val`: Int, var next: ListNode?)

class Solution {
    fun reverseBetween(head: ListNode?, left: Int, right: Int): ListNode? {
        if (left == 1) {
            return reverseFirstN(head, right)
        }

        head?.next = reverseBetween(head?.next, left - 1, right - 1)
        return head
    }

    fun reverseFirstN(head: ListNode?, n: Int): ListNode? {
        if (n == 1) {
            return head
        }

        val next = head?.next
        val newHead = reverseFirstN(next, n - 1)
        head?.next = next?.next
        next?.next = head

        return newHead 
    }
}

fun main() {
    val s = Solution()
    val head = ListNode(1, ListNode(2, ListNode(3, ListNode(4, ListNode(5, null)))))
    var node = s.reverseBetween(head, 2, 4)
    while (node != null) {
        println(node.`val`)
        node = node.next
    }
}

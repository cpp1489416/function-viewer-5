package xyz.cxc6922.functionviewer.core.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.cxc6922.functionviewer.core.node.ConstantNode;
import xyz.cxc6922.functionviewer.core.node.ConstantNode$;
import xyz.cxc6922.functionviewer.core.node.DivideNode;
import xyz.cxc6922.functionviewer.core.node.Node;
import xyz.cxc6922.functionviewer.core.visitors.StringerVisitor;

public class Dog {
    private String name = "ddd";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public String getName() {
        return name;
    }

    public static Node a() {
        DivideNode d = new DivideNode(
                new ConstantNode(-1.0, ConstantNode.Type$.MODULE$.Common()),
                new ConstantNode(ConstantNode.Type$.MODULE$.Common())
        );
        return d;
    }

    public static void main(String[] args) throws Exception {
        Node a = a();
        System.out.println(a.accept(new StringerVisitor()));
        System.out.println(objectMapper.writeValueAsString(a));
    }
}

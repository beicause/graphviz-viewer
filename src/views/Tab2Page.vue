<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>Examples</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true">
      <ion-card>
        <ion-card-header>
          <ion-card-title> <a @click.prevent="openBrowser"
              href="https://renenyffenegger.ch/notes/tools/Graphviz/examples/index">reference</a></ion-card-title>
        </ion-card-header>
      </ion-card>
      <item-example v-for="example in examples" :key="example.title" v-bind="example"></item-example>
    </ion-content>
  </ion-page>
</template>
<script setup>
import { Browser } from '@capacitor/browser';
async function openBrowser() {
  await Browser.open({ url: "https://renenyffenegger.ch/notes/tools/Graphviz/examples/index" })
}
const examples = [{
  title: "Dotted edges etc", content: "This example uses a edge's attribute style to draw a dotted edge.",
  dot: `digraph D {
  A [shape=diamond]
  B [shape=box]
  C [shape=circle]

  A -> B [style=dashed, color=grey]
  A -> C [color="black:invis:black"]
  A -> D [penwidth=5, arrowhead=none]
}`
}, {
  title: "Shape: record vs. plaintext", content: `If the shape attribute is set to record, the text (as controlled with the label attribute) is layouted in tabular form.
The vertical bar (|) starts a new row or column.
The curly braces { ... } change (flip) the direction of |.`,
  dot: `digraph D {
  node [fontname="Arial"];

  node_A [shape=record    label="shape=record|{above|middle|below}|right"];

  node_B [shape=plaintext label="shape=plaintext|{curly|braces and|bars without}|effect"];
}` },
{
  title: "Multiple edges",
  content: " Normally, a dependency (an edge) is declared with an arrow: Node1 -> Node2. Multiple nodes can be listed in curly braces, thus declaring multiple edges in one go. ",
  dot: `digraph D {
  A -> {B, C, D} -> {F}
}` },
{
  title: "Left, mid and right aligned text",
  content: " \l makes the preceding (!) text left aligned, \n makes it centered and \r makes it right aligned. ",
  dot: `digraph L {
  node [shape=record fontname=Arial];

  a  [label="one\ltwo three\lfour five six seven\l"]
  b  [label="one\ntwo three\nfour five six seven"]
  c  [label="one\rtwo three\rfour five six seven\r"]

  a -> b -> c
}` },
{
  title: "Giving the graph a title", content: " With the label attribute, the graph can be given a caption: ",
  dot: `digraph D {
  label = "The foo, the bar and the baz";
  labelloc = "t"; // place the label at the top (b seems to be default)

  node [shape=plaintext]

  FOO -> {BAR, BAZ};
}` }, {
  title: "HTML like labels", content: ` If the text of label is enclosed in <…> rather than in "…", the text is treated as HTML and rendered as HTML like label. `,
  dot: `digraph D {
  label = <The <font color='red'><b>foo</b></font>,<br/> the <font point-size='20'>bar</font> and<br/> the <i>baz</i>>;
  labelloc = "t"; // place the label at the top (b seems to be default)

  node [shape=plaintext]

  FOO -> {BAR, BAZ};
}` }, {
  title: "rank", content: ` { rank=same node_1 node_2 … } specifies that the specified nodes have the same rank, that is, that their distance from the top or left border is equal. `,
  dot: `digraph R {
  node [shape=record];
  { rank=same rA sA tA }
  { rank=same uB vB wB }
  rA -> sA;
  sA -> vB;
  t  -> rA;
  uB -> vB;
  wB -> u;
  wB -> tA;
}` }, {
  title: "Subgraph (clusters)", content: "",
  dot: `digraph Q {

  node [shape=record];

  nd_1   [label = "Node 1"];
  nd_2   [label = "Node 2"];
  nd_3_a [label = "Above Right Node 3"];
  nd_3_l [label = "Left of Node 3"];
  nd_3   [label = "Node 3"];
  nd_3_r [label = "Right of Node 3"];
  nd_4   [label = "Node 4"];

  nd_3_a -> nd_3_r;
  nd_1 -> nd_2 -> nd_3 -> nd_4;

  subgraph cluster_R {
    {rank=same nd_3_l nd_3 nd_3_r}
    nd_3_l -> nd_3 -> nd_3_r [color=grey arrowhead=none];
  }
}` }, {
  title: "Nested clusters", content: "",
  dot: `digraph D {
  subgraph cluster_p {
    label = "Parent";

    subgraph cluster_c1 {
      label = "Child one";
      a;

      subgraph cluster_gc_1 {
        label = "Grand-Child one";
         b;
      }
      subgraph cluster_gc_2 {
        label = "Grand-Child two";
          c;
          d;
      }

    }

    subgraph cluster_c2 {
      label = "Child two";
      e;
    }
  }
}` }, {
  title: "HTML table", content: ` HTML tables are produced if the value of the label attribute is enclosed in <…> rather than "…": `,
  dot: `digraph H {
  aHtmlTable [
   shape=plaintext
   color=blue      // The color of the border of the table
   label=<
     <table border='1' cellborder='0'>
       <tr><td>col 1</td><td>foo</td></tr>
       <tr><td>COL 2</td><td>bar</td></tr>
     </table>
  >];
}` }, {
  title: "Nested HTML table", content: " HTML tables can be nested ",
  dot: `digraph {
  tbl [

    shape=plaintext
    label=<

      <table border='0' cellborder='1' color='blue' cellspacing='0'>
        <tr><td>foo</td><td>bar</td><td>baz</td></tr>
        <tr><td cellpadding='4'>
          <table color='orange' cellspacing='0'>
            <tr><td>one  </td><td>two  </td><td>three</td></tr>
            <tr><td>four </td><td>five </td><td>six  </td></tr>
            <tr><td>seven</td><td>eight</td><td>nine </td></tr>
          </table>
        </td>
        <td colspan='2' rowspan='2'>
          <table color='pink' border='0' cellborder='1' cellpadding='10' cellspacing='0'>
            <tr><td>eins</td><td>zwei</td><td rowspan='2'>drei<br/>sechs</td></tr>
            <tr><td>vier</td><td>fünf</td>                             </tr>
          </table>
        </td> 
        </tr>

        <tr><td>abc</td></tr>

      </table>
    >];
}` },
{
  title: "Colors", content: " A color is specified with one of the attributes that relate to colors. ",
  dot: `digraph D {
  node [shape=plaintext]

  some_node [
   label=<
     <table border="0" cellborder="1" cellspacing="0">
       <tr><td bgcolor="yellow">Foo</td></tr>
       <tr><td bgcolor="lightblue"><font color="#0000ff">Bar</font></td></tr>
       <tr><td bgcolor="#f0e3ff"><font color="#ff1020">Baz</font></td></tr>
     </table>>
  ];
}` },
{
  title: "Rounded box", content: "",
  dot: `digraph H {
  aHtmlTable [
   shape=plaintext
   label=<

     <table border='1' cellborder='0' style='rounded'>
       <tr><td>col 1</td><td>foo</td></tr>
       <tr><td>COL 2</td><td>bar</td></tr>
     </table>

  >];
}` },
{
  title: "Ports", content: "",
  dot: `digraph H {
  parent [
   shape=plaintext
   label=<
     <table border='1' cellborder='1'>
       <tr><td colspan="3">The foo, the bar and the baz</td></tr>
       <tr><td port='port_one'>First port</td><td port='port_two'>Second port</td><td port='port_three'>Third port</td></tr>
     </table>
  >];

  child_one [
   shape=plaintext
   label=<
     <table border='1' cellborder='0'>
       <tr><td>1</td></tr>
     </table>
  >];

  child_two [
   shape=plaintext
   label=<
     <table border='1' cellborder='0'>
       <tr><td>2</td></tr>
     </table>
  >];

  child_three [
   shape=plaintext
   label=<
     <table border='1' cellborder='0'>
       <tr><td>3</td></tr>
     </table>
  >];

  parent:port_one   -> child_one;
  parent:port_two   -> child_two;
  parent:port_three -> child_three;
}` }, {
  title: "Project Dependencies", content: "",
  dot: `digraph D {
  node [shape=plaintext fontname="Sans serif" fontsize="8"];

  task_menu [ label=<
   <table border="1" cellborder="0" cellspacing="1">
     <tr><td align="left"><b>Task 1</b></td></tr>
     <tr><td align="left">Choose Menu</td></tr>
     <tr><td align="left"><font color="darkgreen">done</font></td></tr>
   </table>>];

  task_ingredients [ label=<
   <table border="1" cellborder="0" cellspacing="1">
     <tr><td align="left"><b>Task 2</b></td></tr>
     <tr><td align="left">Buy ingredients</td></tr>
     <tr><td align="left"><font color="darkgreen">done</font></td></tr>
   </table>>];

  task_invitation [ label=<
   <table border="1" cellborder="0" cellspacing="1">
     <tr><td align="left"><b>Task 4</b></td></tr>
     <tr><td align="left">Send invitation</td></tr>
     <tr><td align="left"><font color="darkgreen">done</font></td></tr>
   </table>>];

  task_cook [ label=<
   <table border="1" cellborder="0" cellspacing="1">
     <tr><td align="left"><b>Task 5</b></td></tr>
     <tr><td align="left">Cook</td></tr>
     <tr><td align="left"><font color="red">todo</font></td></tr>
   </table>>];

  task_table[ label=<
   <table border="1" cellborder="0" cellspacing="1">
     <tr><td align="left"><b>Task 3</b></td></tr>
     <tr><td align="left">Lay table</td></tr>
     <tr><td align="left"><font color="red">todo</font></td></tr>
   </table>>];

  task_eat[ label=<
   <table border="1" cellborder="0" cellspacing="1">
     <tr><td align="left"><b>Task 6</b></td></tr>
     <tr><td align="left">Eat</td></tr>
     <tr><td align="left"><font color="red">todo</font></td></tr>
   </table>>];

  task_menu        -> task_ingredients;
  task_ingredients -> task_cook;
  task_invitation  -> task_cook;
  task_table       -> task_eat;
  task_cook        -> task_eat;
}`}
]
</script>
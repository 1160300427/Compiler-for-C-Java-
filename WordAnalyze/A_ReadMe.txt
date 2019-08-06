严格按照文法FA.txt、LL1分析表.txt、SDT.txt执行。这三个文件不可随意更改，否则可能出会错。

FA.txt是词法分析时的规则
LL1.txt,LL1分析表.txt,first.txt,follow.txt,select.txt是语法分析时的规则
tree.txt是语法分析时输出的先根顺序的语法分析树。
SDT.txt是对LL1.txt中的产生式添加语义动作之后的对应项。

执行程序时，必须先执行词法分析器WordAnalyze.java、YufaAnalyze.java后才可执行YuyiAnalyze.java。
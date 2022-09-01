package com.sangeng.controller;


public class AIControllerImpl /*implements AIController*/{
    //AI自动回答
    public String getAnswer(String question){
        //AI核心代码 价值10个亿
        String str = question.replace("吗", "");
        str = str.replace("？","!");
        return str;
    }

    //AI算命
    public String fortuneTelling(String name){
        System.out.println(name);
              //AI算命核心代码
        String[] strs = {"女犯伤官把夫克，旱地莲花栽不活，不是吃上两家饭，也要刷上三家锅。","一朵鲜花头上戴，一年四季也不开，一心想要花开时，采花之人没到来。","此命生来脾气暴，上来一阵双脚跳，对你脾气啥都好，经常与人吵和闹。"};
        int index = name.hashCode() % 3;

        return strs[index];
    }
}

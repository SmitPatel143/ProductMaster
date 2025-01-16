import React, {useState} from 'react'
import {FaBars} from "react-icons/fa6";
import * as AiIcons from "react-icons/ai";
import {Link} from 'react-router-dom';
import { AiOutlineClose } from "react-icons/ai";
import { FaCartArrowDown } from "react-icons/fa";
import Category from "../components/Category.jsx";

export const SideBarData = [
    {
        title: 'Home',
        path: '/Home',
        icon: <AiIcons.AiFillHome />,
        className: 'nav-text'
    },
    {
        title: 'Categories',
        path: '/categories',
        icon: <AiIcons.AiFillProduct />,
        className: 'nav-text'
    },
    {
        title: 'Products',
        path: '/Products',
        icon: <AiIcons.AiFillProduct/>,
        className: 'nav-text'
    },
    {
        title: 'Users',
        path: '/Users',
        icon: <AiIcons.AiOutlineUser />,
        className: 'nav-text'
    },
    {
        title: 'Orders',
        path: '/Orders',
        icon: <FaCartArrowDown />,
        className: 'nav-text'
    },
]


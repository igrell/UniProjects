# cmake_minimum_required(VERSION <specify CMake version here>)
project(dynamic_memory)

set(CMAKE_CXX_STANDARD 14)

add_executable(dynamic_memory
        main.cpp)

add_subdirectory(../google_tests)
#include "Polynomial.h"
#include "iostream"

using std::string, std::cin;

int main() {

    string command, answer;
    int size1, size2, temp_size, eval_val, which_poly;
    Polynomial<double> polynomial1;
    Polynomial<double> polynomial2;
    Polynomial<double> polynomialTemp;
    cout << "Input size for polynomial 1: ";
    cin >> size1;
    polynomial1 = Polynomial<double>(size1);
    for (int i = 0; i < size1; ++i) {
        cin >> polynomial1.coeffs[i];
    }
    cout << "Inputed polynomial 1: " << polynomial1 << "\n";

    cout << "Input size for polynomial 2: ";
    cin >> size2;
    polynomial2 = Polynomial<double>(size2);
    for (int i = 0; i < size2; ++i) {
        cin >> polynomial2.coeffs[i];
    }
    cout << "Inputed polynomial 2: " << polynomial2 << "\n";
    while (true) {

        cout << "Type command (print,change,+,-,*,eval,exit): ";
        cin >> command;
        if (command == "change") {
            cout << "Which polynomial to change? ";
            cin >> which_poly;
            cout << "Input size for new polynomial: ";
            cin >> temp_size;
            polynomialTemp = Polynomial<double>(temp_size);
            for (int i = 0; i < temp_size; ++i) {
                cin >> polynomialTemp.coeffs[i];
            }
            cout << "Inputted new polynomial: " << polynomialTemp << "\n";
            if (which_poly == 1) polynomial1 = polynomialTemp;
            else if (which_poly == 2) polynomial2 = polynomialTemp;
            else cout << "Wrong polynomial index\n";
        } else if (command == "print") {
            cout << "Which polynomial to print? ";
            cin >> which_poly;
            if (which_poly == 1) cout << polynomial1 << "\n";
            else if (which_poly == 2)cout << polynomial2 << "\n";
            else cout << "Wrong polynomial index\n";
        } else if (command == "exit") break;
        else if (command == "+") {
            cout << (polynomial1 + polynomial2) << "\nEvaluate result (y,n)? ";
            cin >> answer;
            if (answer == "y") {
                cout << "At which point? ";
                cin >> eval_val;
                cout << (polynomial1 + polynomial2).evaluate(eval_val) << "\n";
            }
        } else if (command == "-") {
            cout << polynomial1 - polynomial2 << "\nEvaluate result (y,n)? ";
            cin >> answer;
            if (answer == "y") {
                cout << "At which point? ";
                cin >> eval_val;
                cout << (polynomial1 - polynomial2).evaluate(eval_val) << "\n";
            }
        } else if (command == "*") {
            cout << polynomial1 * polynomial2 << "\nEvaluate result (y,n)? ";
            cin >> answer;
            if (answer == "y") {
                cout << "At which point? ";
                cin >> eval_val;
                cout << (polynomial1 * polynomial2).evaluate(eval_val) << "\n";
            }
        } else if (command == "eval") {
            cout << "Which polynomial to evaluate (1,2)? ";
            cin >> which_poly;
            cout << "At what value? ";
            cin >> eval_val;
            if (which_poly == 1) cout << polynomial1.evaluate(eval_val) << "\n";
            else if (which_poly == 2) cout << polynomial2.evaluate(eval_val) << "\n";
            else cout << "Wrong polynomial index\n";
        } else cout << "No such command.\n";

    }

}
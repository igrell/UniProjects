// Igor Piechowiak
#include <iostream>
#include <cstdlib>
#include <cstring>

using namespace std;

int main() {
    unsigned int N;
    cin >> N;
    auto **t = (unsigned int **) malloc(sizeof(unsigned int *) * N);
    auto *p = (unsigned int *) malloc(sizeof(unsigned int) * N);
    for (int i = 0; i < N; ++i) {
        unsigned int M;
        cin >> M;
        *(p + i) = M;
        *(t + i) = (unsigned int *) malloc(sizeof(unsigned int) * M);
        for (int j = 0; j < M; ++j) {
            cin >> *(*(t + i) + j);
        }
    }
    char c;
    while (true) {
        cin >> c;
        if (c == 'P') {
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < *(p + i); ++j) cout << *(*(t + i) + j) << " ";
                cout << endl;
            }
            cout << endl;
        }
        if (c == 'R') {
            unsigned int n;
            cin >> n;
            free(*(t + n));
            for (unsigned int i = n + 1; i < N; ++i) {
                *(t + (i - 1)) = *(t + i);
                *(p + (i - 1)) = *(p + i);
            }
            --N;
            auto **temp = (unsigned int **) malloc(sizeof(unsigned int *) * N);
            auto *temp_p = (unsigned int *) malloc(sizeof(unsigned int) * N);
            memcpy(temp, t, sizeof(unsigned int *) * N);
            memcpy(temp_p, p, sizeof(unsigned int *) * N);
            free(t);
            free(p);
            t = temp;
            p = temp_p;
        }
        if (c == 'S') {
            unsigned int n, m;
            cin >> n >> m;
            unsigned int *temp = *(t + n), temp1 = *(p + n);
            *(t + n) = *(t + m);
            *(t + m) = temp;
            *(p + n) = *(p + m);
            *(p + m) = temp1;
        }
        if (c == 'D') {
            unsigned int n;
            cin >> n;
            auto *temp = (unsigned int *) malloc(sizeof(unsigned int) * ((*(p + n)) * 2));
            memcpy(temp, *(t + n), sizeof(unsigned int) * (*(p + n)));
            memcpy(temp + (*(p + n)), *(t + n), sizeof(unsigned int) * (*(p + n)));
            free(*(t + n));
            *(t + n) = temp;
            *(p + n) *= 2;
        }
        if (c == 'A') {
            unsigned int n;
            cin >> n;
            auto **temp = (unsigned int **) malloc(sizeof(unsigned int *) * (N + 1));
            auto *temp_p = (unsigned int *) malloc(sizeof(unsigned int) * (N + 1));
            memcpy(temp, t, sizeof(unsigned int *) * N);
            memcpy(temp_p, p, sizeof(unsigned int) * N);
            memcpy((temp_p + N), (p + n), sizeof(unsigned int));
            *(temp + N) = (unsigned int *) malloc(sizeof(unsigned int) * (*(temp_p + N)));
            memcpy(*(temp + N), *(t + n), sizeof(unsigned int) * (*(p + n)));
            free(t);
            free(p);
            t = temp;
            p = temp_p;
            ++N;
        }
        if (c == 'I') {
            unsigned int n;
            unsigned int m;
            unsigned int k;
            cin >> n;
            cin >> m;
            cin >> k;
            auto *temp = (unsigned int *) malloc(sizeof(unsigned int) * ((*(p + n)) + (*(p + m))));
            memcpy(temp, *(t + m), sizeof(unsigned int) * k);
            memcpy(temp + k, *(t + n), sizeof(unsigned int) * (*(p + n)));
            memcpy(temp + k + (*(p + n)), *(t + m) + k, sizeof(unsigned int) * ((*(p + m)) - k));
            *(p + m) = *(p + m) + *(p + n);
            free(*(t + m));
            *(t + m) = temp;
            free(*(t + n));
            for (unsigned int i = n + 1; i < N; ++i) {
                *(t + (i - 1)) = *(t + i);
                *(p + (i - 1)) = *(p + i);
            }
            --N;
        }
        if (c == 'E') {
            for (int i = 0; i < N; ++i) {
                free(*(t + i));
            }
            free(t);
            free(p);
            return 0;
        }
    }
}
